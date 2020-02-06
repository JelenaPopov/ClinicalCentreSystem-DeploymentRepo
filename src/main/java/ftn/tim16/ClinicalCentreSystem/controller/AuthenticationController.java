package ftn.tim16.ClinicalCentreSystem.controller;

import ftn.tim16.ClinicalCentreSystem.dto.request.UserDTO;
import ftn.tim16.ClinicalCentreSystem.dto.requestandresponse.PatientDTO;
import ftn.tim16.ClinicalCentreSystem.dto.response.ChangePasswordDTO;
import ftn.tim16.ClinicalCentreSystem.dto.response.LoggedInUserDTO;
import ftn.tim16.ClinicalCentreSystem.security.auth.JwtAuthenticationRequest;
import ftn.tim16.ClinicalCentreSystem.service.AuthenticationService;
import ftn.tim16.ClinicalCentreSystem.serviceimpl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping(value = "/api/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserServiceImpl userService;

    @PostMapping(value = "/register")
    public ResponseEntity<PatientDTO> registerPatient(@RequestBody PatientDTO patientDTO) {
        PatientDTO patient = authenticationService.registerPatient(patientDTO);
        if (patient == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(patient, HttpStatus.CREATED);
    }

    @PostMapping(value = "/login")
    public ResponseEntity<LoggedInUserDTO> login(@RequestBody JwtAuthenticationRequest authenticationRequest) throws AuthenticationException, IOException {

        try {
            LoggedInUserDTO loggedInUserDTO = authenticationService.login(authenticationRequest);
            if (loggedInUserDTO == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(loggedInUserDTO, HttpStatus.OK);
        } catch (AuthenticationException ex) {
            if (userService.neverLoggedIn(authenticationRequest.getUsername())) {
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ChangePasswordDTO> changePassword(@Valid @RequestBody UserDTO userDTO) {

        UserDetails newUser = userService.changePassword(userDTO);

        if (newUser == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ChangePasswordDTO(newUser), HttpStatus.CREATED);
    }


}
