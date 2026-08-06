package com.vivekvarma1.moviebooking.show.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DebugController {

    @GetMapping("/test")
    public String test() {
        System.out.println("TEST HIT");
        return "OK";
    }
}