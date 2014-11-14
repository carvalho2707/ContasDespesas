/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pt.tiago.contasdespesas.security;

import java.io.Serializable;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import pt.tiago.contasdespesas.peopleDetails.PeopleDetailsByNBFetch;

/**
 *
 * @author NB20198
 */
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider,
        Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Autowired
    PeopleDetailsByNBFetch peopleDetailsByNBFetch;

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();
        peopleDetailsByNBFetch.setUserDetails(name, password);
        List<GrantedAuthority> grantedAuths = new ArrayList<GrantedAuthority>();
        return new UsernamePasswordAuthenticationToken(name, password,
                grantedAuths);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
