package com.vivekvarma1.moviebooking.ticket.service;

public interface QRCodeService {

    byte[] generateQRCode(String content);

}