package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class TransactionController {

    @Autowired
    private TransactionRepository transactionRepo;





}
