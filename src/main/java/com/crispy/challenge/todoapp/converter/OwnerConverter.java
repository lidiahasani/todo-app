package com.crispy.challenge.todoapp.converter;

import com.crispy.challenge.todoapp.dto.OwnerDto;
import com.crispy.challenge.todoapp.model.Owner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class OwnerConverter {

    private final PasswordEncoder passwordEncoder;

    public OwnerConverter(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public OwnerDto toOwnerDto(Owner owner) {
        return new OwnerDto(owner.getUsername(), owner.getPassword());
    }

    public Owner toOwner(OwnerDto ownerDto) {
        Owner owner = new Owner();
        owner.setUsername(ownerDto.username());
        owner.setPassword(passwordEncoder.encode(ownerDto.password()));
        return owner;
    }

}
