package com.vivekvarma1.moviebooking.config;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import com.vivekvarma1.moviebooking.booking.dto.response.BookingResponse;
import com.vivekvarma1.moviebooking.booking.entity.Booking;
import com.vivekvarma1.moviebooking.booking.repository.BookingRepository;
import com.vivekvarma1.moviebooking.booking.service.BookingService;
import com.vivekvarma1.moviebooking.common.customExceptionHandler.BookingOwnershipException;
import com.vivekvarma1.moviebooking.common.customExceptionHandler.InvalidBookingStateException;
import com.vivekvarma1.moviebooking.common.customExceptionHandler.resourceNotFoundException.BookingNotFoundException;
import com.vivekvarma1.moviebooking.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    @Value("${razorpay.key.id}")
    private String keyId;

    @Value("${razorpay.key.secret}")
    private String keySecret;

    private final BookingRepository bookingRepository;
    private final BookingService bookingService;

    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder(
            @AuthenticationPrincipal User user,
            @RequestBody Map<String, Object> data
    ) {
        try {
            Long bookingId = Long.parseLong(data.get("bookingId").toString());

            Booking booking = bookingRepository.findById(bookingId)
                    .orElseThrow(() -> new BookingNotFoundException(bookingId));

            if (!booking.getUser().getId().equals(user.getId())) {
                throw new BookingOwnershipException();
            }
            if (!booking.isPaymentStarted()) {
                throw new InvalidBookingStateException(
                        "Booking is not awaiting payment."
                );
            }

            BigDecimal totalAmount = booking.getTotalAmount();
            int amountInPaise = totalAmount.multiply(new BigDecimal("100")).intValue();

            RazorpayClient razorpay = new RazorpayClient(keyId, keySecret);

            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", amountInPaise);
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", "txn_booking_" + bookingId);

            JSONObject notes = new JSONObject();
            notes.put("bookingId", bookingId.toString());
            orderRequest.put("notes", notes);

            Order order = razorpay.orders.create(orderRequest);

            Map<String, Object> response = new HashMap<>();
            response.put("orderId", order.get("id"));
            response.put("currency", order.get("currency"));
            response.put("amount", order.get("amount"));
            response.put("keyId", keyId);
            response.put("bookingId", bookingId);

            return ResponseEntity.ok(response);
        } catch (BookingOwnershipException | InvalidBookingStateException | BookingNotFoundException e) {
            throw e; // let your global exception handler format these consistently
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(Map.of("message", "Error creating payment order: " + e.getMessage()));
        }
    }

    @PostMapping("/verify-payment")
    public ResponseEntity<?> verifyPayment(
            @AuthenticationPrincipal User user,
            @RequestBody Map<String, String> data
    ) throws RazorpayException {
        String orderId = data.get("razorpay_order_id");
        String paymentId = data.get("razorpay_payment_id");
        String signature = data.get("razorpay_signature");
        Long bookingId = Long.parseLong(data.get("bookingId"));

        JSONObject options = new JSONObject();
        options.put("razorpay_order_id", orderId);
        options.put("razorpay_payment_id", paymentId);
        options.put("razorpay_signature", signature);

        boolean isSignatureValid = Utils.verifyPaymentSignature(options, keySecret);

        if (!isSignatureValid) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Invalid payment signature."));
        }

        BookingResponse bookingResponse = bookingService.confirmBooking(user.getId(), bookingId);

        return ResponseEntity.ok(Map.of(
                "status", "SUCCESS",
                "message", "Payment verified and booking confirmed successfully.",
                "booking", bookingResponse
        ));
    }
}