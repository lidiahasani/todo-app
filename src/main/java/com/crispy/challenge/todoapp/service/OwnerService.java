package com.crispy.challenge.todoapp.service;

import com.crispy.challenge.todoapp.dto.OwnerDto;

public interface OwnerService {
    OwnerDto create(OwnerDto ownerDto);

    String login(OwnerDto ownerDto);

    void logout(String token);
}
