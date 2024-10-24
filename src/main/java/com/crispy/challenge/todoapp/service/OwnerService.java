package com.crispy.challenge.todoapp.service;

import com.crispy.challenge.todoapp.dto.OwnerDto;

public interface OwnerService {
    void create(OwnerDto ownerDto);

    String login(OwnerDto ownerDto);

    void logout(String token);
}
