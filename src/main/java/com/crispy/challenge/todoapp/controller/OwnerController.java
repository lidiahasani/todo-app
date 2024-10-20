package com.crispy.challenge.todoapp.controller;

import com.crispy.challenge.todoapp.dto.OwnerDto;
import com.crispy.challenge.todoapp.service.OwnerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/owner")
public class OwnerController {

    private final Logger logger = LoggerFactory.getLogger(OwnerController.class);

    private final OwnerService ownerService;

    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @PostMapping
    public ResponseEntity<OwnerDto> create(@RequestBody OwnerDto ownerDto) {
        logger.info("Creating owner {}.", ownerDto);
        var result = ownerService.create(ownerDto);
        logger.info("Created owner {}.", result);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }
}
