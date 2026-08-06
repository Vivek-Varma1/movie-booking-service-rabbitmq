package com.vivekvarma1.moviebooking.show.service;

import com.vivekvarma1.moviebooking.common.customExceptionHandler.ResourceNotFoundException;
import com.vivekvarma1.moviebooking.common.customExceptionHandler.resourceNotFoundException.MovieNotFoundException;
import com.vivekvarma1.moviebooking.common.customExceptionHandler.resourceNotFoundException.ScreenNotFoundException;
import com.vivekvarma1.moviebooking.event.entity.Movie;
import com.vivekvarma1.moviebooking.event.respository.MovieRepository;
import com.vivekvarma1.moviebooking.show.dto.response.ShowResponse;
import com.vivekvarma1.moviebooking.show.dto.request.CreateShowRequest;
import com.vivekvarma1.moviebooking.show.entity.Show;
import com.vivekvarma1.moviebooking.show.entity.ShowStatus;
import com.vivekvarma1.moviebooking.show.mapper.ShowMapper;
import com.vivekvarma1.moviebooking.show.repository.ShowRepository;
import com.vivekvarma1.moviebooking.theatre.entity.Screen;
import com.vivekvarma1.moviebooking.theatre.entity.Seat;
import com.vivekvarma1.moviebooking.theatre.repository.ScreenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ShowServiceImpl implements ShowService {

    private final ShowRepository showRepository;
    private final MovieRepository movieRepository;
    private final ScreenRepository screenRepository;
    private final ShowMapper showMapper;

    @Override
    public ShowResponse createShow(CreateShowRequest request) {
        System.out.println("========== CREATE SHOW ==========");
        Movie movie = movieRepository.findById(request.movieId())
                .orElseThrow(() ->
                        new MovieNotFoundException(
                                request.movieId()
                        ));

        Screen screen = screenRepository.findById(request.screenId())
                .orElseThrow(() ->
                        new ScreenNotFoundException(
                                request.screenId()
                        ));

        boolean exists = showRepository
                .existsByScreenIdAndShowDateAndShowSlot(
                        screen.getId(),
                        request.showDate(),
                        request.showSlot()
                );

        if (exists) {
            throw new IllegalStateException(
                    "Show already exists for this screen and slot."
            );
        }

        Show show = new Show(
                movie,
                screen,
                request.showDate(),
                request.showSlot(),
                ShowStatus.OPEN_FOR_BOOKING
        );

        screen.getSeats()
                .forEach(
                        seat -> show.addShowSeat(
                                seat,
                                seat.getSeatCategory()
                                        .getBasePrice()
                        )
                );

        return showMapper.toResponse(
                showRepository.save(show)
        );

//        Show savedShow = showRepository.save(show);
//
//        return showMapper.toResponse(savedShow);
    }

    private BigDecimal determinePrice(Seat seat) {
        return seat.getSeatCategory().getBasePrice();
//        return seat.getSeatCategory().getBasePrice()
//        .multiply(movie.getPricingMultiplier());
    }
//

    @Override
    @Transactional(readOnly = true)
    public ShowResponse getShow(long showId) {

        Show show = showRepository.findById(showId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Show",
                                "showId",
                                showId
                        ));

        return showMapper.toResponse(show);
    }
    @Transactional(readOnly = true)
    @Override
    public List<ShowResponse> getAllShows() {
        log.info("Today = {}", LocalDate.now());
        return showRepository.findByShowDateAfter(LocalDate.now())
                .stream()
                .map(showMapper::toResponse)
                .toList();
    }
    @Override
    public void deleteShow(Long showId) {

        Show show = showRepository.findById(showId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Show",
                                "showId",
                                showId
                        ));

        showRepository.delete(show);
    }
}