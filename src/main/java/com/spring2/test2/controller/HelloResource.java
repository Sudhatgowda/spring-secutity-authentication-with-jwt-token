package com.spring2.test2.controller;



import com.spring2.test2.model.AuthenticationRequest;
import com.spring2.test2.model.AuthenticationResponse;
import com.spring2.test2.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloResource {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private MyUserDetailsService myUserDetailsService;
    @Autowired
    private JwtUtil JwtTokenUtil;


    @RequestMapping ( "/" )
    public String hello() {
        return "THIS IS MY MAVEN PROJECT, WITH SECURITY AND JWT";
    }

    @RequestMapping ( value = "/authenticate", method = RequestMethod.POST )
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
        }
        catch (BadCredentialsException e) {

            throw new Exception("Incorrect username and password", e);
        }
        final UserDetails userDetails = myUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = JwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new AuthenticationResponse(jwt));

    }
}

