package szeliga71.pl.wp.galeriawnetrz_ver1.dto;

import java.util.List;

public class CreateUserRequest {
    private String username;
    private String password;
    private List<String> roles; // e.g. ["SUPERADMIN"] or ["ADMIN"]

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
