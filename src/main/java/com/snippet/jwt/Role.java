package com.snippet.jwt;

public class Role {

    private String roleName;
    private String[] privileges;
    
    public String getRoleName() {
        return roleName;
    }
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
    public String[] getPrivileges() {
        return privileges;
    }
    public void setPrivileges(String[] privileges) {
        this.privileges = privileges;
    }

}
