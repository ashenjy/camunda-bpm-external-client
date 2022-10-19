package com.cn.camunda.controller;

import com.cn.camunda.model.JwtRequest;
import com.cn.camunda.model.JwtResponse;
import com.cn.camunda.auth.jwt.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@CrossOrigin
public class JwtAuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService jwtInMemoryUserDetailsService;

//    @Autowired
//    private PasswordEncoder passwordEncoder;

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest)
            throws Exception {

        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
//        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//        if(!manager.userExists(authenticationRequest.getUsername())){
//            throw new UsernameNotFoundException("User not found with username: " + authenticationRequest.getUsername());
//        }
//        final UserDetails userDetails = manager.loadUserByUsername(authenticationRequest.getUsername());

        final UserDetails userDetails = jwtInMemoryUserDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());;

        final String token = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new JwtResponse(token));
    }

//    private UserDetails validateCredentials(String username, String password) throws Exception {
//        UserDetails userDetails = jwtInMemoryUserDetailsService.loadUserByUsername(username);
//
//        if(userDetails != null){
//            if(userDetails.getPassword().equals(password)){
//                return userDetails;
//            }
//            log.error("ERROR: Password Invalid");
//            throw new BadCredentialsException("INVALID_CREDENTIALS");
//        } else {
//            log.error("ERROR: User not found with username: " + username);
//            throw new UsernameNotFoundException("User not found with username: " + username);
//        }
//    }

    private void authenticate(String username, String password) throws Exception {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
