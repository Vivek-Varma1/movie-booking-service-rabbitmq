package com.vivekvarma1.moviebooking.theatre.service;

import com.vivekvarma1.moviebooking.common.customExceptionHandler.ApiException;
import com.vivekvarma1.moviebooking.common.customExceptionHandler.ResourceAlreadyExistsException;
import com.vivekvarma1.moviebooking.common.customExceptionHandler.resourceNotFoundException.ScreenNotFoundException;
import com.vivekvarma1.moviebooking.common.customExceptionHandler.resourceNotFoundException.TheatreNotFoundException;
import com.vivekvarma1.moviebooking.show.dto.response.ShowResponse;
import com.vivekvarma1.moviebooking.show.repository.ShowRepository;
import com.vivekvarma1.moviebooking.theatre.dto.request.CreateScreenRequest;
import com.vivekvarma1.moviebooking.theatre.dto.request.SeatGenerationRuleRequest;
import com.vivekvarma1.moviebooking.theatre.dto.response.ScreenResponse;
import com.vivekvarma1.moviebooking.theatre.dto.response.ScreenSummaryResponse;
import com.vivekvarma1.moviebooking.theatre.entity.Screen;
import com.vivekvarma1.moviebooking.theatre.entity.Seat;
import com.vivekvarma1.moviebooking.theatre.entity.SeatCategory;
import com.vivekvarma1.moviebooking.theatre.entity.Theatre;
import com.vivekvarma1.moviebooking.theatre.mapper.ScreenMapper;
import com.vivekvarma1.moviebooking.theatre.repository.ScreenRepository;
import com.vivekvarma1.moviebooking.theatre.repository.TheatreRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class ScreenServiceImpl implements ScreenService {

    private final ScreenRepository screenRepository;
    private final TheatreRepository theatreRepository;
    private final ScreenMapper screenMapper;
    private final ShowRepository showRepository;

    @Override
    public ScreenResponse createScreen(
            Long theatreId,
            CreateScreenRequest request
    ) {

        Theatre theatre = getTheatreOrThrow(theatreId);

        validateScreenRequest(
                theatre,
                theatreId,
                request
        );

        Screen screen = createScreenAggregate(
                theatre,
                request
        );

        generateSeats(
                screen,
                request
        );
       // screen.assignShowSlots(request.standardShowTimes());
        Screen savedScreen=screenRepository.saveAndFlush(screen);

        return screenMapper.toResponse(savedScreen);
    }
    private Theatre getTheatreOrThrow(
            Long theatreId
    ) {
        return theatreRepository.findById(theatreId)
                .orElseThrow(
                        () -> new TheatreNotFoundException(
                                theatreId
                        )
                );
    }

    private SeatCategory getCategoryForRow(
            char row,
            List<SeatGenerationRuleRequest> rules
    ) {

        for(SeatGenerationRuleRequest rule : rules) {

            char start =
                    rule.fromRow().toUpperCase().charAt(0);

            char end =
                    rule.toRow().toUpperCase().charAt(0);

            if(row >= start && row <= end) {
                return rule.seatCategory();
            }
        }

        throw new ApiException(
                "No seat category configured for row " + row
        );
    }

    @Override
    @Transactional(readOnly = true)
    public ScreenResponse getScreen(Long screenId) {

        Screen screen = screenRepository.findById(screenId)
                .orElseThrow(() ->
                        new ScreenNotFoundException(screenId));

        return screenMapper.toResponse(screen);
    }
    private void validateScreenRequest(
            Theatre theatre,
            Long theatreId,
            CreateScreenRequest request
    ) {

        validateScreenName(
                theatre,
                theatreId,
                request.name()
        );

        validateLastRow(
                request.lastRow()
        );

        validateSeatRules(
                request
        );
//        validateShowSlots(
//                request.standardShowTimes()
//        );
    }
//    private void validateShowSlots(List<LocalTime> standardShowTimes) {
//
//        if (standardShowTimes == null || standardShowTimes.isEmpty()) {
//            throw new ApiException(
//                    "At least one standard show time must be configured for the screen"
//            );
//        }
//
//        Set<LocalTime> unique = new HashSet<>(standardShowTimes);
//        if (unique.size() != standardShowTimes.size()) {
//            throw new ApiException(
//                    "Duplicate show slot times are not allowed"
//            );
//        }
//    }
    private void validateScreenName(
            Theatre theatre,
            Long theatreId,
            String screenName
    ) {

        String normalizedName = screenName.trim();

        if (screenRepository.existsByTheatreIdAndName(
                theatreId,
                normalizedName
        )) {
            throw new ResourceAlreadyExistsException(
                    "Screen '" +
                            normalizedName +
                            "' already exists in theatre '" +
                            theatre.getName() +
                            "'"
            );
        }
    }
    private void validateLastRow(
            String lastRow
    ) {

        if (!lastRow.matches("[A-Z]")) {
            throw new ApiException(
                    "Last row must be a single alphabet from A to Z."
            );
        }
    }
    private void validateSeatRules(
            CreateScreenRequest request
    ) {

        Set<Character> assignedRows = new HashSet<>();

        char maxRow =
                request.lastRow()
                        .toUpperCase()
                        .charAt(0);

        for (SeatGenerationRuleRequest rule : request.seatRules()) {

            validateSingleSeatRule(
                    rule,
                    maxRow,
                    assignedRows
            );
        }

        validateCompleteCoverage(
                assignedRows,
                maxRow
        );
    }

    private void validateSingleSeatRule(
            SeatGenerationRuleRequest rule,
            char maxRow,
            Set<Character> assignedRows
    ) {

        if (!rule.fromRow().matches("[A-Z]")
                || !rule.toRow().matches("[A-Z]")) {
            throw new ApiException(
                    "Seat rule rows must be single alphabets from A to Z."
            );
        }

        char start =
                rule.fromRow()
                        .toUpperCase()
                        .charAt(0);

        char end =
                rule.toRow()
                        .toUpperCase()
                        .charAt(0);

        if (start > end) {
            throw new ApiException(
                    "Invalid seat rule: "
                            + rule.fromRow()
                            + " cannot come after "
                            + rule.toRow()
            );
        }

        if (start > maxRow || end > maxRow) {
            throw new ApiException(
                    "Seat rule "
                            + rule.fromRow()
                            + "-"
                            + rule.toRow()
                            + " exceeds configured last row "
                            + maxRow
            );
        }

        for (char row = start; row <= end; row++) {

            if (!assignedRows.add(row)) {

                throw new ApiException(
                        "Row "
                                + row
                                + " is assigned to multiple seat categories."
                );
            }
        }
    }
    private void validateCompleteCoverage(
            Set<Character> assignedRows,
            char maxRow
    ) {

        for (char row = 'A'; row <= maxRow; row++) {

            if (!assignedRows.contains(row)) {

                throw new ApiException(
                        "No seat category configured for row "
                                + row
                );
            }
        }
    }
    private Screen createScreenAggregate(
            Theatre theatre,
            CreateScreenRequest request
    ) {

        Screen screen = new Screen(
                request.name().trim()
        );

        theatre.addScreen(
                screen
        );

        return screen;
    }
    private void generateSeats(
            Screen screen,
            CreateScreenRequest request
    ) {

        char lastRow =
                request.lastRow()
                        .toUpperCase()
                        .charAt(0);

        for (char row = 'A';
             row <= lastRow;
             row++) {

            SeatCategory category =
                    getCategoryForRow(
                            row,
                            request.seatRules()
                    );

            for (int seatNumber = 1;
                 seatNumber <= request.seatsPerRow();
                 seatNumber++) {

                Seat seat = new Seat(
                        String.valueOf(row),
                        seatNumber,
                        category
                );

                screen.addSeat(
                        seat
                );
            }
        }
    }
    @Transactional
    @Override
    public void deleteScreen(
            Long theatreId,
            Long screenId
    ) {

        Screen screen = screenRepository
                .findByIdAndTheatreId(
                        screenId,
                        theatreId
                )
                .orElseThrow(() ->
                        new ScreenNotFoundException(
                                screenId
                        )
                );

        if (showRepository.existsByScreenId(
                screenId
        )) {

            throw new ApiException(
                    "Cannot delete screen '" +
                            screen.getName() +
                            "' because shows are scheduled for it."
            );
        }

        screenRepository.delete(screen);
    }
    @Transactional(readOnly = true)
    @Override
    public List<ScreenSummaryResponse> getScreensByTheatre(
            Long theatreId
    ) {

        if (!theatreRepository.existsById(
                theatreId
        )) {
            throw new TheatreNotFoundException(
                    theatreId
            );
        }

        List<Screen> screens =
                screenRepository.findByTheatreIdOrderByNameAsc(
                        theatreId
                );

        return screenMapper.toSummaryResponses(
                screens
        );
    }
//    @Override
//    public ScreenResponse createScreen(
//            Long theatreId,
//            CreateScreenRequest request
//    ) {
//
//        Theatre theatre = theatreRepository.findById(theatreId)
//                .orElseThrow(() ->
//                        new ResourceNotFoundException(
//                                "Theatre",
//                                "theatreId",
//                                theatreId
//                        )
//                );
//
//        Screen screen = new Screen(
//                request.name()
//        );
//
//        theatre.addScreen(screen);
//
//        screenRepository.save(screen);
//
//        return screenMapper.toResponse(screen);
//    }

//    @Override
//    @Transactional(readOnly = true)
//    public ScreenResponse getScreen(Long screenId) {
//
//        Screen screen = screenRepository.findById(screenId)
//                .orElseThrow(()->new ScreenNotFoundException(screenId));
//
//        List<SeatResponse> seats =
//                screen.getSeats()
//                        .stream()
//                        .map(seat -> new SeatResponse(
//                                seat.getId(),
//                                seat.getSeatLabel(),
//                                seat.getSeatCategory()
//                        ))
//                        .toList();
//
//        return screenMapper.toResponse(screen);
//    }
}