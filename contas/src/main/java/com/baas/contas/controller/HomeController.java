package com.baas.contas.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    String home() {
        return "Olá, obrigado pela oportunidade de aprender cada dia mais com vocês!";
    }

}
