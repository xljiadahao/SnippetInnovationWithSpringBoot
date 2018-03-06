package com.snippet.jwt;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class JwtUtilTest {

    private User user;
    
    @Before
    public void setUp() {
        user = new User();
        Role role = new Role();
        role.setRoleName("admin");
        role.setPrivileges(new String[] {"read","write","delete"});
        user.setId(33l);
        user.setUserName("lei");
        user.setRole(role);
    }
    
    @Test
    public void jwtTest() throws Exception {
        // JWT signing
        String jwt = JwtUtil.sign(user, 10800000l);
        System.out.println("jwt: " + jwt);
        String[] splitJwt = jwt.split("\\.");
        assertEquals(3, splitJwt.length);
        // JWT parsing
        User jwtUserInfo = JwtUtil.unsign(jwt, User.class);
        System.out.println("userId: " + jwtUserInfo.getId() + ", userName: " 
                + jwtUserInfo.getUserName() + ", role: " + jwtUserInfo.getRole().getRoleName());
        assertEquals(33l, jwtUserInfo.getId());
        assertEquals("lei", jwtUserInfo.getUserName());
        assertEquals("admin", jwtUserInfo.getRole().getRoleName());
        assertEquals(3, jwtUserInfo.getRole().getPrivileges().length);
    }

}
