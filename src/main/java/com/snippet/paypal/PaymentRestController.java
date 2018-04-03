package com.snippet.paypal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class PaymentRestController {

    private static final String PP_MERCHANT_URL = "<base URL>";
    
    @RequestMapping(value="/paywithpaypal", method=RequestMethod.POST)
    @ResponseBody
    public PaymentData payWithPaypal(HttpServletRequest servletRequest, HttpServletResponse servletResponse, 
            @RequestBody Map<String, Object> requestBody) {
        String merchantId = (String) requestBody.get("merchantId");
        String merchantName = (String) requestBody.get("merchantName");
        String orderId = (String) requestBody.get("orderId");
        Double amount = (Double) requestBody.get("amount");
        System.out.println("Pay with PayPal: " + merchantId + ", " 
                + merchantName + ", " + orderId + ", " + amount);
        PaymentData paymentData = new PaymentData();
        paymentData.setMerchantId(merchantId);
        paymentData.setMerchantName(merchantName);
        paymentData.setBillId(orderId);
        paymentData.setAmount(amount);
        paymentData.setStatus(updateMerchantPaymentStatus(merchantId, orderId, amount));
        if (PaymentStatus.PAID != paymentData.getStatus()) {
            paymentData.setErrMsg("rolling back the transaction");
        }
        return paymentData;
    }
    
    private static PaymentStatus updateMerchantPaymentStatus(String merchantId, String orderId, Double amount) {
        RestTemplate restClient = new RestTemplate();
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        List<MediaType> media = new ArrayList<>();
        media.add(MediaType.ALL);
        converter.setSupportedMediaTypes(media);
        messageConverters.add(converter);
        restClient.setMessageConverters(messageConverters);
        if (StringUtils.isBlank(PP_MERCHANT_URL)) {
            System.out.println("error, PP_MERCHANT_URL is empty, rolling back the transaction");
            return PaymentStatus.ROLLBACK;
        }
        StringBuilder urlBuilder = new StringBuilder(PP_MERCHANT_URL);
        urlBuilder.append("/merchantportal").append("/pay.rest");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        MerchantPaymentData params = new MerchantPaymentData();
        params.setOrderId(orderId);
        params.setMerchantId(merchantId);
        HttpEntity<MerchantPaymentData> requestEntity = new HttpEntity<MerchantPaymentData>(params, headers);
        try {
            ResponseEntity<Boolean> response = restClient.exchange(urlBuilder.toString(), HttpMethod.POST, requestEntity, Boolean.class);
            System.out.println("merchantportal response: " + response.getBody());
            if (response.getBody() == null) {
                return PaymentStatus.ROLLBACK;
            }
            if (response.getBody()) {
                return PaymentStatus.PAID;
            } else {
                return PaymentStatus.ROLLBACK;
            }
        } catch (Exception ex) {
            return PaymentStatus.ROLLBACK;
        }
    }

}
