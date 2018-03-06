package com.snippet.jwt;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.internal.com.fasterxml.jackson.databind.ObjectMapper;

public class JwtUtil {

    // secret
    private static final String SECRET = "jsonwebtoken";
    // registered claims
    private static final String EXPIRATION_TIME = "exp";
    private static final String ISSUER = "iss";
    private static final String SUBJECT = "sub";
    // private claims
    private static final String CUSTOM_CLAIMS = "customclaims";
    
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static<T> String sign(T object, long maxAge) throws Exception {
        final JWTSigner signer = new JWTSigner(SECRET);
        final Map<String, Object> claims = new HashMap<String, Object>();
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(object);
        claims.put(CUSTOM_CLAIMS, jsonString);
        claims.put(EXPIRATION_TIME, System.currentTimeMillis() + maxAge);
        claims.put(ISSUER, "auth0");
        claims.put(SUBJECT, "third party authorization");
        return signer.sign(claims);
    }
    
    public static<T> T unsign(String jwt, Class<T> classT) throws Exception {
        final JWTVerifier verifier = new JWTVerifier(SECRET);
        final Map<String,Object> claims= verifier.verify(jwt);
        if (claims.containsKey(EXPIRATION_TIME) && claims.containsKey(ISSUER) 
                && claims.containsKey(SUBJECT) && claims.containsKey(CUSTOM_CLAIMS)) {
            long exp = (long) claims.get(EXPIRATION_TIME);
            System.out.println("EXPIRATION_TIME: " + sdf.format(new Date(exp)) + ", ISSUER: " 
                    + (String) claims.get(ISSUER) + ", SUBJECT: " + (String) claims.get(SUBJECT));
            if (exp > System.currentTimeMillis()) {
                String privateClaims = (String) claims.get(CUSTOM_CLAIMS);
                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.readValue(privateClaims, classT);
            }
        }
        return null;
    }

}
