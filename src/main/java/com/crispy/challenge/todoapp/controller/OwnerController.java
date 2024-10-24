package com.crispy.challenge.todoapp.controller;

import com.crispy.challenge.todoapp.dto.OwnerDto;
import com.crispy.challenge.todoapp.service.OwnerService;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/owner")
public class OwnerController {

    private final Logger logger = LoggerFactory.getLogger(OwnerController.class);

    private final OwnerService ownerService;

    public OwnerController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @Operation(summary = "Sign up")
    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.OK)
    public void signUp(@RequestBody OwnerDto ownerDto) {
        logger.debug("Creating owner {}.", ownerDto);
        ownerService.create(ownerDto);
        logger.debug("Owner created successfully.");
    }

    @Operation(summary = "Log In")
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public String login(@RequestBody OwnerDto ownerDto){
        logger.debug("Attempting to login with username: {}", ownerDto.username());
        var result = ownerService.login(ownerDto);
        logger.debug("Logged in successfully with username: {}", ownerDto.username());
        return result;
    }

    @Operation(summary = "Log out")
    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(@RequestHeader("Authorization") String token){
        logger.debug("Attempting to logout.");
        ownerService.logout(token);
        logger.debug("Logged out successfully.");
    }
}
