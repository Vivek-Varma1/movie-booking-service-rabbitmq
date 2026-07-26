package com.vivekvarma1.moviebooking.ticket.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.vivekvarma1.moviebooking.ticket.service.QRCodeService;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
@Service
public class QRCodeServiceImpl implements QRCodeService {

    private static final int WIDTH = 300;
    private static final int HEIGHT = 300;

    @Override
    public byte[] generateQRCode(String content) {

        try {

            QRCodeWriter writer = new QRCodeWriter();

            BitMatrix matrix = writer.encode(
                    content,
                    BarcodeFormat.QR_CODE,
                    WIDTH,
                    HEIGHT
            );

            ByteArrayOutputStream output = new ByteArrayOutputStream();

            MatrixToImageWriter.writeToStream(
                    matrix,
                    "PNG",
                    output
            );

            return output.toByteArray();

        } catch (Exception ex) {

            throw new RuntimeException("Unable to generate QR Code", ex);

        }

    }

}