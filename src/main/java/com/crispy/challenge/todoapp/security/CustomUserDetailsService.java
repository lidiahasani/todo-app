package com.crispy.challenge.todoapp.security;

import com.crispy.challenge.todoapp.exception.NoResultFoundException;
import com.crispy.challenge.todoapp.model.Owner;
import com.crispy.challenge.todoapp.repository.OwnerRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private static final String ROLE_USER = "ROLE_USER";
    private final OwnerRepository ownerRepository;

    public CustomUserDetailsService(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Owner owner = ownerRepository.findByUsername(username)
                .orElseThrow(() -> new NoResultFoundException("User not found"));

        return new CustomUserDetails(owner.getId(),
                owner.getUsername(),
                owner.getPassword(),
                getAuthorities()
        );
    }

    private static Set<GrantedAuthority> getAuthorities() {
        return Set.of(new SimpleGrantedAuthority(ROLE_USER));
    }
}
