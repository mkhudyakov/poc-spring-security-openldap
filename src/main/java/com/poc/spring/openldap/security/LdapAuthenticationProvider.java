package com.poc.spring.openldap.security;

import com.poc.spring.openldap.security.model.UserDetails;
import com.poc.spring.openldap.security.provider.LdapUserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LdapAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private LdapUserProvider userProvider;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        final String username = authentication.getName();
        final String password = authentication.getCredentials().toString();

        if (!userProvider.authenticate(username, password)) {
            throw new UsernameNotFoundException("User not found");
        }

        /* If user found then get user details */
        UserDetails userDetails = userProvider.getByUsername(username);
        List<GrantedAuthority> authorities = userProvider.getUserGroups(username)
                .stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(
                username, null, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
