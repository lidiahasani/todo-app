package com.crispy.challenge.todoapp.service.impl;

import com.crispy.challenge.todoapp.converter.OwnerConverter;
import com.crispy.challenge.todoapp.dto.OwnerDto;
import com.crispy.challenge.todoapp.model.Owner;
import com.crispy.challenge.todoapp.repository.OwnerRepository;
import com.crispy.challenge.todoapp.service.OwnerService;
import com.crispy.challenge.todoapp.security.TokenService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class OwnerServiceImpl implements OwnerService {

    private final AuthenticationManager authenticationManager;

    private final OwnerRepository ownerRepository;

    private final OwnerConverter ownerConverter;
    private final TokenService tokenService;

    public OwnerServiceImpl(AuthenticationManager authenticationManager, OwnerRepository ownerRepository, OwnerConverter ownerConverter, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.ownerRepository = ownerRepository;
        this.ownerConverter = ownerConverter;
        this.tokenService = tokenService;
    }

    @Override
    public void create(OwnerDto ownerDto) {
        Owner owner = ownerConverter.toOwner(ownerDto);
        ownerRepository.save(owner);
    }

    @Override
    public String login(OwnerDto ownerDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(ownerDto.username(), ownerDto.password()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return tokenService.generateToken(authentication);
    }

    @Override
    public void logout(String token) {
        String jwt = token.substring(7);
        tokenService.revokeToken(jwt);
    }
}
