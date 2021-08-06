package com.wikigreen.wikistorage.security;

import com.wikigreen.wikistorage.model.User;
import com.wikigreen.wikistorage.security.jwt.JwtUserFactory;
import com.wikigreen.wikistorage.service.UserService;
import com.wikigreen.wikistorage.service.exceptions.CustomEntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class JwtUserDetailsService implements UserDetailsService {
    private final UserService userService;

    @Autowired
    public JwtUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            User user = userService.getByNickName(username);
            return JwtUserFactory.createJwtUser(user);
        } catch (CustomEntityNotFoundException exception) {
            throw new UsernameNotFoundException(String.format("User with username '%s' is not found", exception.getPropertyValue()));
        }
    }
}
