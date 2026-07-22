package com.vivekvarma1.moviebooking.booking.locking.service;

import com.vivekvarma1.moviebooking.theatre.entity.Seat;
import com.vivekvarma1.moviebooking.show.entity.Show;
import com.vivekvarma1.moviebooking.user.entity.User;

import java.util.List;

public interface ISeatLockProvider {
	    void lockSeats(Show show, List<Seat> seat, User user) throws Exception;
	    void unlockSeats(Show show, List<Seat> seat, User user);
	    boolean validateLock(Show show, Seat seat, User user);
	    List<Seat> getLockedSeats(Show show);
	}