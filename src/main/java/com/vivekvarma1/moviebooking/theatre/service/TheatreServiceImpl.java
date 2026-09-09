package com.vivekvarma1.moviebooking.theatre.service;

import com.vivekvarma1.moviebooking.common.customExceptionHandler.ApiException;
import com.vivekvarma1.moviebooking.common.customExceptionHandler.ResourceAlreadyExistsException;
import com.vivekvarma1.moviebooking.common.customExceptionHandler.ResourceNotFoundException;


import com.vivekvarma1.moviebooking.common.customExceptionHandler.resourceNotFoundException.TheatreNotFoundException;
import com.vivekvarma1.moviebooking.theatre.dto.request.CreateTheatreRequest;
import com.vivekvarma1.moviebooking.theatre.dto.request.UpdateTheatreRequest;
import com.vivekvarma1.moviebooking.theatre.dto.response.TheatreResponse;
import com.vivekvarma1.moviebooking.theatre.dto.response.TheatreSummaryResponse;
import com.vivekvarma1.moviebooking.theatre.entity.City;
import com.vivekvarma1.moviebooking.theatre.entity.Theatre;
import com.vivekvarma1.moviebooking.theatre.mapper.TheatreMapper;
import com.vivekvarma1.moviebooking.theatre.repository.CityRepository;
import com.vivekvarma1.moviebooking.theatre.repository.TheatreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TheatreServiceImpl implements TheatreService {
    private final TheatreRepository theatreRepository;
    private final TheatreMapper theatreMapper;
    private final CityRepository cityRepository;

    @Override
    public TheatreResponse createTheatre(
            CreateTheatreRequest request
    ) {
        City city =
                cityRepository.findById(
                                request.cityId()
                        )
                        .orElseThrow(
                                () -> new ResourceNotFoundException("City","CityId",
                                        request.cityId()
                                )
                        );

        validateTheatreRequest(
                request,
                city
        );

        Theatre theatre =
                createTheatreAggregate(
                        request,
                        city
                );

        Theatre savedTheatre =
                theatreRepository.save(
                        theatre
                );

        return theatreMapper.toResponse(
                savedTheatre
        );
    }
    private void validateTheatreRequest(
            CreateTheatreRequest request,
            City city
    ) {
        validateTheatreUniqueness(request,city);
    }
    private void validateTheatreUniqueness(
            CreateTheatreRequest request,
            City city
    ) {

        if (theatreRepository.existsByNameIgnoreCaseAndCityId(
                request.name().trim(),
                city.getId()
        )) {
            throw new ResourceAlreadyExistsException(
                    "Theatre already exists in this city."
            );

        }
    }
    private Theatre createTheatreAggregate(
            CreateTheatreRequest request,
            City city
    ) {

        return new Theatre(
                request.name().trim(),
                request.address().trim(),
                city
        );
    }
//    private void validateName(
//            String name
//    ) {
//
//        if (name.length() < 3) {
//            throw new ApiException(
//                    "Theatre name must contain at least 3 characters."
//            );
//        }
//    }
//    private void validateCity(
//            String city
//    ) {
//        if (!city.matches("[A-Za-z ]+")) {
//            throw new ApiException(
//                    "Invalid city name."
//            );
//        }
////    }
//    private String normalize(
//            String value
//    ) {
//        return value.trim();
//    }

    @Override
    @Transactional(readOnly=true)
    public TheatreResponse getTheatre(Long theatreId) {
        Theatre theatre=theatreRepository.findById(theatreId).
                orElseThrow(()->new TheatreNotFoundException(theatreId));

        return theatreMapper.toResponse(theatre);
    }
    @Override
    @Transactional(readOnly = true)
    public List<TheatreSummaryResponse> getAllTheatres() {
        return theatreRepository.findAll().stream()
                .map(theatre -> new TheatreSummaryResponse(
                        theatre.getId(),
                        theatre.getName(),
                        theatre.getAddress(),
                        theatre.getCity().getId(),
                        theatre.getCity().getName()
                ))
                .toList();
    }
    @Override
    @Transactional
    public TheatreResponse updateTheatre(Long theatreId, UpdateTheatreRequest request) {
        Theatre theatre = theatreRepository.findById(theatreId)
                .orElseThrow(() -> new ResourceNotFoundException("Theatre", "theatreId", theatreId));

        City city = cityRepository.findById(request.cityId())
                .orElseThrow(() -> new ResourceNotFoundException("City", "cityId", request.cityId()));

        theatre.updateDetails(request.name(), request.address(), city);
        // ^ add this method to Theatre entity if it doesn't have one - simple
        // setters for name/address/city, called together so there's one
        // entry point for updates (same pattern as Booking's state methods)

        Theatre saved = theatreRepository.save(theatre);
        return theatreMapper.toResponse(saved); // or however TheatreResponse is built elsewhere
    }

    @Override
    @Transactional
    public void deleteTheatre(Long theatreId) {
        Theatre theatre = theatreRepository.findById(theatreId)
                .orElseThrow(() -> new ResourceNotFoundException("Theatre", "theatreId", theatreId));

        if (screenRepository.existsByTheatre_Id(theatreId)) {
            throw new IllegalStateException(
                    "Cannot delete theatre '" + theatre.getName()
                            + "' - it still has screens. Remove those screens first."
            );
        }

        theatreRepository.delete(theatre);
    }
}
