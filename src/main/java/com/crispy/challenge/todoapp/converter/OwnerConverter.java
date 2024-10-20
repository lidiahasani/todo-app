package com.crispy.challenge.todoapp.converter;

import com.crispy.challenge.todoapp.dto.OwnerDto;
import com.crispy.challenge.todoapp.model.Owner;
import org.springframework.stereotype.Component;

@Component
public class OwnerConverter {

    public OwnerDto toOwnerDto(Owner owner) {
        return new OwnerDto(owner.getUsername(), owner.getPassword());
    }

    public Owner toOwner(OwnerDto ownerDto) {
        Owner owner = new Owner();
        owner.setUsername(ownerDto.username());
        owner.setPassword(ownerDto.password());
        return owner;
    }

}
