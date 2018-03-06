package com.snippet.jwt;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JsonWebTokenController {

    private static final String NAME = "lei";
    private static final String PWD = "pwd";
    
    @RequestMapping(value="/login", method=RequestMethod.POST)
    @ResponseBody
    public TokenResponseData login(HttpServletRequest servletRequest, 
            HttpServletResponse servletResponse, @RequestBody Map<String, Object> requestBody){
        String userName = (String) requestBody.get("user_name");
        String pwd = (String) requestBody.get("password");
        if (NAME.equalsIgnoreCase(userName) && PWD.equalsIgnoreCase(pwd)) {
            Role role = new Role();
            role.setRoleName("admin");
            role.setPrivileges(new String[] {"read","write","delete"});
            User user = new User();
            user.setId(33l);
            user.setUserName(userName);
            user.setRole(role);
            String jwt = null;
            try {
                jwt = JwtUtil.sign(user, 10800000l);
            } catch (Exception e) {
                System.out.println("Login unexpected error: " + e.getMessage());
                return null;
            }
            TokenResponseData tokenResponse = new TokenResponseData();
            tokenResponse.setUserName(userName);
            tokenResponse.setToken(jwt);
            Map<String, String> reqParam = new HashMap<String, String>();
            reqParam.put("token", jwt);
            tokenResponse.add(linkTo(methodOn(JsonWebTokenController.class).verify(reqParam)).withRel("verify"));
            return tokenResponse;
        }
        return null;
    }
    
    @RequestMapping(value="/verify", method=RequestMethod.GET)
    @ResponseBody
    public User verify(@RequestParam Map<String, String> allRequestParams) {
        String jwt = allRequestParams.get("token");
        try {
            return JwtUtil.unsign(jwt, User.class);
        } catch (Exception e) {
            System.out.println("Verify unpextected error, " + e.getMessage());
            return null;
        }
    }

}
