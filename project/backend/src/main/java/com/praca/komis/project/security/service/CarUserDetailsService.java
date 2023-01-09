package com.praca.komis.project.security.service;

import com.praca.komis.project.model.User;
import com.praca.komis.project.repository.UserRepository;
import com.praca.komis.project.security.model.CarUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarUserDetailsService implements UserDetailsService {

    @Autowired
    private final UserRepository userRepository;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.
                findById(Long.valueOf(username))
                .orElseThrow();

        if(!user.getEnabled()) throw new UsernameNotFoundException("Account is not enabled!");

        var carUserDetails = new CarUserDetails(user.getUsername(), user.getPassword(),
                user.getAuthorities().stream()
                        .map(role -> (GrantedAuthority) role::name)
                        .collect(Collectors.toList()));

        carUserDetails.setId(user.getId());

        return carUserDetails;
    }

}
