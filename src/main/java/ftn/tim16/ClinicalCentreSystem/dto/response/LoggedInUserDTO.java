package ftn.tim16.ClinicalCentreSystem.dto.response;

import ftn.tim16.ClinicalCentreSystem.model.UserTokenState;

public class LoggedInUserDTO {
    private Long id;

    private String email;

    private String role;

    private UserTokenState userTokenState;

    public LoggedInUserDTO(Long id, String email, String role, UserTokenState userTokenState) {
        this.id = id;
        this.email = email;
        this.role = role;
        this.userTokenState = userTokenState;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public UserTokenState getUserTokenState() {
        return userTokenState;
    }

    public void setUserTokenState(UserTokenState userTokenState) {
        this.userTokenState = userTokenState;
    }
}
