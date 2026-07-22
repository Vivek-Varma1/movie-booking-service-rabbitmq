package com.vivekvarma1.moviebooking.show.service;

import com.vivekvarma1.moviebooking.common.customExceptionHandler.InvalidShowSeatException;
import com.vivekvarma1.moviebooking.common.customExceptionHandler.ResourceNotFoundException;
import com.vivekvarma1.moviebooking.common.customExceptionHandler.SeatAlreadyBookedException;
import com.vivekvarma1.moviebooking.common.customExceptionHandler.SeatAlreadyLockedException;
import com.vivekvarma1.moviebooking.common.customExceptionHandler.resourceNotFoundException.ShowNotFoundException;
import com.vivekvarma1.moviebooking.show.dto.request.LockSeatsRequest;
import com.vivekvarma1.moviebooking.show.dto.response.LockSeatsResponse;
import com.vivekvarma1.moviebooking.show.dto.response.SeatRowResponse;
import com.vivekvarma1.moviebooking.show.dto.response.ShowSeatLayoutResponse;
import com.vivekvarma1.moviebooking.show.entity.Show;
import com.vivekvarma1.moviebooking.show.entity.ShowSeat;
import com.vivekvarma1.moviebooking.show.mapper.ShowSeatMapper;
import com.vivekvarma1.moviebooking.show.repository.ShowRepository;
import com.vivekvarma1.moviebooking.show.repository.ShowSeatRepository;
import com.vivekvarma1.moviebooking.user.entity.User;
import com.vivekvarma1.moviebooking.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShowSeatServiceImpl implements ShowSeatService {

    private final ShowRepository showRepository;
    private final ShowSeatRepository showSeatRepository;
    private final ShowSeatMapper showSeatMapper;
    private final UserRepository userRepository;
    private static final int LOCK_DURATION_MINUTES = 1;

    @Override
    public ShowSeatLayoutResponse getSeatLayout(
            Long showId
    ) {

        Show show = showRepository.findById(showId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Show",
                                "showId",
                                showId
                        )
                );

        List<ShowSeat> showSeats =
                showSeatRepository.findByShowIdOrderBySeatRowAscSeatSeatNumberAsc(
                        showId
                );

        List<SeatRowResponse> rows =
                showSeats.stream()
                        .collect(Collectors.groupingBy(
                                showSeat -> showSeat.getSeat().getRow(),
                                LinkedHashMap::new,
                                Collectors.mapping(
                                        showSeatMapper::toResponse,
                                        Collectors.toList()
                                )
                        ))
                        .entrySet()
                        .stream()
                        .map(entry ->
                                new SeatRowResponse(
                                        entry.getKey(),
                                        entry.getValue()
                                )
                        )
                        .toList();

//        return new ShowSeatLayoutResponse(
//                show.getId(),
//                show.getMovie().getMovieName(),
//                show.getScreen().getTheatre().getName(),
//                show.getScreen().getName(),
//                rows
//        );
        return new ShowSeatLayoutResponse(
                show.getId(),
                show.getMovie().getMovieName(),
                show.getShowDate(),
                show.getShowSlot(),
                show.getScreen().getTheatre().getName(),
                show.getScreen().getName(),
                rows
        );
    }

    @Override
    @Transactional
    public LockSeatsResponse lockSeats(
            LockSeatsRequest request
    ) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User",
                                "userId",
                                request.userId()
                        )
                );

        List<ShowSeat> showSeats =
                showSeatRepository.findAllByShowIdAndIdIn(
                        request.showId(),
                        request.showSeatIds()
                );

        if (showSeats.size() != request.showSeatIds().size()) {
           throw new InvalidShowSeatException();
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime lockedUntil = now.plusMinutes(LOCK_DURATION_MINUTES);

        for (ShowSeat showSeat : showSeats) {

//            if (showSeat.isBooked()) {
//                throw new SeatAlreadyBookedException(
//                        showSeat.getSeat().getSeatLabel()
//                );
//            }

            if (showSeat.isBooked()) {
                throw new SeatAlreadyBookedException(
                        showSeat.getSeat().getSeatLabel()
                );
            }

            if (showSeat.isLockExpired()) {
                showSeat.unlock();
            }

            if (showSeat.isLocked()) {
                throw new SeatAlreadyLockedException(
                        showSeat.getSeat().getSeatLabel()
                );
            }

            showSeat.lock(user, lockedUntil);

        }

        return new LockSeatsResponse(
                showSeats.stream()
                        .map(ShowSeat::getId)
                        .toList(),
                lockedUntil
        );
    }
    @Transactional(readOnly = true)
    @Override
    public List<Long> getShowSeatIds(Long showId) {

        if (!showRepository.existsById(showId)) {
            throw new ShowNotFoundException( showId
            );
        }

        return showSeatRepository.findIdsByShowId(showId);
    }

}