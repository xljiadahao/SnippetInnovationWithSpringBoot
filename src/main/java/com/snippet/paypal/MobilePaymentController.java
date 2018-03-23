package com.snippet.paypal;

import java.util.HashMap;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MobilePaymentController {

    @RequestMapping(value = {"","/pay"}, method = RequestMethod.GET)
    public String payment(HashMap<String,Object> reqParam) {
        System.out.println("inside pay");
        return "pay";
    }

}