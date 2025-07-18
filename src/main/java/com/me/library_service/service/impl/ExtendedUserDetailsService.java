package com.me.library_service.service.impl;

import com.me.library_service.model.enums.ResponseCode;
import com.me.library_service.persistence.entity.User;
import com.me.library_service.persistence.entity.UserRole;
import com.me.library_service.persistence.repository.UserRepository;
import com.me.library_service.persistence.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExtendedUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByEmail(username);
        if(optionalUser.isEmpty()){
            throw new UsernameNotFoundException(ResponseCode.INVALID_USER.getMessage());
        }
        User dbUser = optionalUser.get();

        List<UserRole> userRoles = userRoleRepository.findAllByUser(dbUser);
        Set<SimpleGrantedAuthority> grantedAuthorities = userRoles.stream()
                .map(userRole -> new SimpleGrantedAuthority(userRole.getRole().getName()))
                .collect(Collectors.toSet());

        UserDetails user = new org.springframework.security.core.userdetails.User(dbUser.getEmail(), dbUser.getPassword(), grantedAuthorities);
        return user;
    }
}