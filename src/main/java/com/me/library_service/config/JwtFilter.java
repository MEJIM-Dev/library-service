package com.me.library_service.config;

import com.me.library_service.model.enums.ResponseCode;
import com.me.library_service.persistence.entity.User;
import com.me.library_service.persistence.entity.UserRole;
import com.me.library_service.persistence.repository.UserRepository;
import com.me.library_service.persistence.repository.UserRoleRepository;
import com.me.library_service.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(request.getServletPath().startsWith("/auth")){
            filterChain.doFilter(request,response);
            return;
        }
        String authorization = request.getHeader("Authorization");
        if(authorization==null || !authorization.substring(0,7).matches("Bearer ")) {
            doFilter(request, response, filterChain);
            return;
        }

        String bearerToken = authorization.substring(7);
        String username = jwtUtil.getSubject(bearerToken);
        if(username!=null && SecurityContextHolder.getContext().getAuthentication() == null){
            Optional<User> optionalUser = userRepository.findByEmail(username);
            if(optionalUser.isEmpty()){
                throw new UsernameNotFoundException(ResponseCode.INVALID_USER.getMessage());
            }
            User user = optionalUser.get();
            boolean validJwt = jwtUtil.validateJwt(bearerToken, user);

            if(validJwt) {
                List<UserRole> roles = userRoleRepository.findAllByUser(user);

                Set<SimpleGrantedAuthority> grantedAuthorities = roles.stream().map(role -> {
                    return new SimpleGrantedAuthority("ROLE_"+role.getRole().getName());
                }).collect(Collectors.toSet());
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getEmail(),bearerToken, grantedAuthorities);
                authenticationToken.setDetails(request);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }

        }
        doFilter(request, response, filterChain);
    }
}
