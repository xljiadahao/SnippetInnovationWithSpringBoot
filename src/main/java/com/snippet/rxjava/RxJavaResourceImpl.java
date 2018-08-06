package com.snippet.rxjava;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;

import org.apache.commons.configuration2.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.netty.buffer.ByteBuf;
import io.reactivex.netty.RxNetty;
import io.reactivex.netty.protocol.http.client.HttpClientRequest;
import io.reactivex.netty.protocol.http.client.HttpClientResponse;
import rx.Observable;
import rx.Observer;
import rx.Scheduler;
import rx.schedulers.Schedulers;

/**
 * This resource shows how to use JAX-RS injection and how to use a spring bean
 * as a JAX-RS resource class.
 *
 * Notice that the scope for this bean is request which means that a new
 * instance of this class will be created per request.
 */

@Component
@Scope("request")
public class RxJavaResourceImpl implements RxJavaResource {

    //@Inject
    // @EndPoint(service = "bankreferenceserv")
    private WebTarget bankserv;
    //@Inject
    // @AsyncEndPoint(service = "bankreferenceserv")
    private RxNetty calendarserv;
    private List<String> respList = new ArrayList<String>();
    private final Scheduler s = rx.schedulers.Schedulers.io();
    private final Scheduler s2 = rx.schedulers.Schedulers.io();
    private final Scheduler s3 = rx.schedulers.Schedulers.newThread();

    @Override
    public String sayHello() {
        return "Hello, World!";
    }
    @Override
    public Greeting sayGreet() {
        Greeting greeting = new Greeting(10, "Hello ", 200.0);
        return greeting;
    }
    @Override
    public Greeting createGreet(Greeting greeting) {
        greeting.setGreet("This is from server");
        return greeting;
    }

    @Override
    public String getCalendar() {
        WebTarget bankservice = bankserv.path("/v1/calendar/workdays")
                .queryParam("year", "2017").queryParam("country", "US");
        Response resp = bankservice.request(MediaType.APPLICATION_JSON).get();
        int status = resp.getStatus();
        if (status >= 200 && status < 300) {
            String holidays = resp.readEntity(String.class);
            return holidays;
        }
        return "";
    }
    @Override
    public void getCalendarAsync(AsyncResponse asyncResponse) {
        StringBuffer responseBuffer = new StringBuffer();
        // Set the service path
        String calendarServPath = "/v1/calendar/workdays?country=US&year=2017";
        String calendarServPath2 = "/v1/calendar/workdays?country=AU&year=2017";
        String calendarServPath3 = "/v1/calendar/workdays?country=IN&year=2017";
        // create an request object
        HttpClientRequest<?> httpClientRequest = HttpClientRequest.createGet(calendarServPath);
        HttpClientRequest<?> httpClientRequest2 = HttpClientRequest.createGet(calendarServPath2);
        HttpClientRequest<?> httpClientRequest3 = HttpClientRequest.createGet(calendarServPath3);
        asyncResponse.resume(null);
        /**
         * for code reference
         */
        // create the observable
//        Observable<JacksonHttpClientResponse> rxNettyObservable = calendarserv
//                .createHystrixObservable(JacksonHttpClientResponse.class, httpClientRequest);
//        Observable<JacksonHttpClientResponse> rxNettyObservable2 = calendarserv
//                .createHystrixObservable(JacksonHttpClientResponse.class, httpClientRequest2);
//        Observable<JacksonHttpClientResponse> rxNettyObservable3 = calendarserv
//                .createHystrixObservable(JacksonHttpClientResponse.class, httpClientRequest3);
//        rxNettyObservable.observeOn(s).mergeWith(rxNettyObservable2.observeOn(s2))
//                .mergeWith(rxNettyObservable3.observeOn(s3)) // give up the web container thread
//                .subscribe(new Observer<JacksonHttpClientResponse>() {
//                    @Override
//                    public void onCompleted() {
//                        Response myResponse;
//                        myResponse = Response.status(200).entity(respList).type(MediaType.TEXT_PLAIN).build();
//                        asyncResponse.resume(myResponse);
//                    }
//                    @Override
//                    public void onError(Throwable e) {
//                        // TODO Application logic goes here to decide what to
//                        String message = (e.getMessage() == null) ? "No error message available " : e.getMessage();
//                        Response myResponse = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(message)
//                                .type(MediaType.TEXT_PLAIN).build();
//                        asyncResponse.resume(myResponse);
//                    }
//                    @Override
//                    public void onNext(JacksonHttpClientResponse response) {
//                        // TODO Application logic goes here
//                        Response myResponse;
//                        System.out.println("&&&&&&&&&&&&&&&&&&&&&&" + Thread.currentThread().getName());
//                        int responseCode = response.getResponseCode().code();
//                        System.out.println(response);
//                        try {
//                            // deserialize the response content into a map Map<?, ?> details = response.getContent(Map.class);
//                            String resp = new String(response.getContent().toByteArray());
//                            respList.add(resp);
//                        } catch (Exception e) {
//                            responseBuffer
//                                    .append((e.getMessage() == null) ? "No error message available " : e.getMessage());
//                        }
//                    }
//                });
    }

    @Override
    public Greeting luck() {
        System.out.println("luck");
        Date datetime = new Date();
        String timeStr = String.valueOf(datetime.getTime());
        int luckyNum = Integer.parseInt(timeStr.substring(timeStr.length() - 2));
        int random = new Random().nextInt(10);
        System.out.println("luckyNum: " + luckyNum + ", random: " + random);
        Greeting luck = new Greeting();
        luck.setId(luckyNum);
        luck.setSal(random);
        luck.setDatetime(datetime);
        if (random != 0 && luckyNum % random == 0) {
            luck.setGreet("Great! How lucky you are!");
        } else {
            luck.setGreet("Sorry. Try next time.");
        }
        return luck;
    }
    @Override
    public String tryLuck() throws Exception {
        System.out.println("try luck " + Thread.currentThread().getName());
        StringBuilder str = new StringBuilder();
        RxNetty.createHttpGet("http://localhost:8080/rxjavaresource/luck")
        .subscribeOn(Schedulers.io()).doOnNext(resp -> {
            resp.getContent().forEach(buf -> {
                str.append(buf.toString(Charset.forName("UTF-8")));
                System.out.println("&&&&&&&&&&&&&&&&&&&&&& " + Thread.currentThread().getName());
                System.out.println(buf.toString(Charset.forName("UTF-8")));
            });
        }).subscribe();
        System.out.println("debug: wait for 1 sec");
        Thread.sleep(1000);
        System.out.println("debug: " + str.toString());
        return str.toString();
    }
    @Override
    public void tryLuckAsync(AsyncResponse asyncResponse) throws Exception {
        List<Greeting> resultList = new ArrayList<>();
        StringBuilder str = new StringBuilder();
        String tryLuckRes = "http://localhost:8080/rxjavaresource/luck";
        HttpClientRequest<ByteBuf> httpClientRequest = HttpClientRequest.createGet(tryLuckRes);
        HttpClientRequest<ByteBuf> httpClientRequest2 = HttpClientRequest.createGet(tryLuckRes);
        HttpClientRequest<ByteBuf> httpClientRequest3 = HttpClientRequest.createGet(tryLuckRes);
        Observable<HttpClientResponse<ByteBuf>> rxNettyObservable = RxNetty.createHttpRequest(httpClientRequest);
        Observable<HttpClientResponse<ByteBuf>> rxNettyObservable2 = RxNetty.createHttpRequest(httpClientRequest2);
        Observable<HttpClientResponse<ByteBuf>> rxNettyObservable3 = RxNetty.createHttpRequest(httpClientRequest3);
//        rxNettyObservable.observeOn(s).mergeWith(rxNettyObservable2.observeOn(s2))
//                .mergeWith(rxNettyObservable3.observeOn(s3))
        rxNettyObservable.observeOn(s).subscribe(new Observer<HttpClientResponse<ByteBuf>>(){
            @Override
            public void onCompleted() {
                System.out.println("debug async completed: " + str.toString());
                asyncResponse.resume(Response.status(200).entity(resultList).type(MediaType.APPLICATION_JSON).build());
            }
            @Override
            public void onError(Throwable arg0) {
                System.out.println("onError " + arg0.getMessage());
            }
            @Override
            public void onNext(HttpClientResponse<ByteBuf> resp) {
                System.out.println("debug async next " + Thread.currentThread().getName());
                resp.getContent().subscribe(s->{
                    ObjectMapper objectMapper = new ObjectMapper();
                    Greeting greeting = null;
                    try {
                        greeting = objectMapper.readValue(s.toString(Charset.forName("UTF-8")), Greeting.class);
                        System.out.println("debug async content: " + greeting.getGreet());
                        resultList.add(greeting);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }});
    }

}