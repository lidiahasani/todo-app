package com.crispy.challenge.todoapp.service.impl;

import com.crispy.challenge.todoapp.converter.OwnerConverter;
import com.crispy.challenge.todoapp.dto.OwnerDto;
import com.crispy.challenge.todoapp.model.Owner;
import com.crispy.challenge.todoapp.repository.OwnerRepository;
import com.crispy.challenge.todoapp.service.OwnerService;
import org.springframework.stereotype.Service;

@Service
public class OwnerServiceImpl implements OwnerService {

    private final OwnerRepository ownerRepository;

    private final OwnerConverter ownerConverter;

    public OwnerServiceImpl(OwnerRepository ownerRepository, OwnerConverter ownerConverter) {
        this.ownerRepository = ownerRepository;
        this.ownerConverter = ownerConverter;
    }

    @Override
    public OwnerDto create(OwnerDto ownerDto) {
        Owner owner = ownerConverter.toOwner(ownerDto);
        owner = ownerRepository.save(owner);
        return ownerConverter.toOwnerDto(owner);
    }
}
