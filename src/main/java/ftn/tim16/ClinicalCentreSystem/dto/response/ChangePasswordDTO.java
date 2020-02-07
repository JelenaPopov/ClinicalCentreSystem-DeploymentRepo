package ftn.tim16.ClinicalCentreSystem.dto.response;

import org.springframework.security.core.userdetails.UserDetails;

public class ChangePasswordDTO {
    private String email;

    private String password;


    public ChangePasswordDTO(UserDetails userDetails) {
        email = userDetails.getUsername();
        password = userDetails.getPassword();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
