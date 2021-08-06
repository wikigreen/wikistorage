package com.wikigreen.wikistorage.security.jwt;

import com.wikigreen.wikistorage.model.Role;
import com.wikigreen.wikistorage.model.User;
import com.wikigreen.wikistorage.model.UserStatus;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtUserFactory {

    public static JwtUser createJwtUser(User user){
        return new JwtUser(
                user.getId(),
                user.getNickName(),
                user.getPassword(),
                UserStatus.ACTIVE.equals(user.getUserStatus()),
                mapToGrantedAuthorities(user.getRoles())
        );
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(List<Role> roles){
        return roles.stream()
                .map(Role::getRoleName)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
