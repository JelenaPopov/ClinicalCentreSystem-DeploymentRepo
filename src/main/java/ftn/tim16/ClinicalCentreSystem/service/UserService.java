package ftn.tim16.ClinicalCentreSystem.service;

import ftn.tim16.ClinicalCentreSystem.dto.request.UserDTO;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {
    UserDetails changePassword(UserDTO userDTO);

    Object findUserByEmail(String email);

    boolean neverLoggedIn(String email);

}
