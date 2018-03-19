package com.snippet.jwt;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import com.snippet.jwt.qrcode.Merchant;
import com.snippet.jwt.qrcode.QRCodeGenerator;

public class QRCodeTokenTest {

    private Merchant merchant;
    
    @Before
    public void setUp() {
        merchant = new Merchant();
        merchant.setId(33l);
        merchant.setName("Starbucks Suntec");
        merchant.setPaypalAccount("starbucks@paypal.com");
        merchant.setUrl("www.paypal.com");
    }
    
    @Test
    public void qrCodeGeneratorTest() throws Exception {
        File logo = new File("qrcode/paypal_logo.png");
        String jwt = JwtUtil.sign(merchant, 86400000l);
        System.out.println("jwt: " + jwt);
        String[] splitJwt = jwt.split("\\.");
        assertEquals(3, splitJwt.length);
        String qrCodeHex = QRCodeGenerator.generateQRCode(logo, "PayPalSG", jwt);
        System.out.println("length of QR Code hex: " + qrCodeHex.length());
        assertTrue(qrCodeHex.length() > 0);
    }

}
