package com.neo.userservice.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CustomJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        String rolesClaim = jwt.getClaimAsString("roles"); // comma-separated, e.g. "ADMIN" or "ROLE_SERVICE"
        List<String> roleValues = (rolesClaim == null || rolesClaim.isBlank())
                ? Collections.emptyList()
                : Arrays.asList(rolesClaim.split(","));

        Collection<GrantedAuthority> authorities = roleValues.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(j -> authorities);
        return converter.convert(jwt);
    }
}