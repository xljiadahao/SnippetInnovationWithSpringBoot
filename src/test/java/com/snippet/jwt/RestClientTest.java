package com.snippet.jwt;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestOperations;

import com.snippet.jwt.client.RestClientConfig;
import com.snippet.jwt.qrcode.Merchant;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RestClientConfig.class)
public class RestClientTest {

    @Autowired
    private RestOperations restClient;

    private String testUrl;
    private String testToken;

    @Before
    public void setUp() {
        testUrl = "https://<url>/tokenmgt/ppverify?token={token}";
        testToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9."
                + "eyJpc3MiOiJQYXlQYWwiLCJzdWIiOiJPMk8gdGh"
                + "pcmQgcGFydHkgYXV0aG9yaXphdGlvbiIsImN1c3"
                + "RvbWNsYWltcyI6IntcImlkXCI6MzMsXCJuYW1lX"
                + "CI6XCJTdGFyYnVja3MgU3VudGVjXCIsXCJ1cmxc"
                + "IjpcInd3dy5wYXlwYWwuY29tXCIsXCJwYXlwYWx"
                + "BY2NvdW50XCI6XCJzdGFyYnVja3NAcGF5cGFsLm"
                + "NvbVwifSIsImV4cCI6MTUyMTYzNTU5MTc4M30.f"
                + "JLXKIlc6X4oc0ism6WnJgnCUmFvvaXu9Lw8c-ByD9Y";
    }

    /**
     * deploy the war file to the server, 
     * set service URL above and SSL keystore details in the config.properties,
     * then enable the Unit Test.
     */
    // @Test
    public void restClientTest() throws Exception {
        Merchant merchant = verifyToken(testToken);
        assertEquals(33l, merchant.getId());
        assertEquals("Starbucks Suntec", merchant.getName());
        assertEquals("www.paypal.com", merchant.getUrl());
    }

    private Merchant verifyToken(String token) {
        ResponseEntity<Merchant> response = restClient.getForEntity(testUrl, Merchant.class, token);
        return response.getBody();
    }

}
