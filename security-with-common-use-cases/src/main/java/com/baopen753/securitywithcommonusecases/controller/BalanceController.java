package com.baopen753.securitywithcommonusecases.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BalanceController {

    @GetMapping("/myBalance")
    public String myBalance() {
        return "Here is my balance number";
    }

}
