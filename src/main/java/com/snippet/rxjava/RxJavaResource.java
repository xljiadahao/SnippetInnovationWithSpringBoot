package com.snippet.rxjava;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;


/**
 * Sample interface that shows how a service can be implemented with a separate
 * interface. This interface can be used as programmatic contract.
 */
@Path("/rxjavaresource")
public interface RxJavaResource {

    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @Path("/luck")
    public Greeting luck();
    
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @Path("/tryluck")
    public String tryLuck() throws Exception;
    
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @Path("/tryluckasync")
    public void tryLuckAsync(@Suspended AsyncResponse asyncResponse) throws Exception;
    
    @GET
    @Produces({ MediaType.TEXT_PLAIN })
    @Path("/hello")
    public String sayHello();
    
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @Path("/calendar")
    public String getCalendar();
    
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @Path("/calendarasync")
    public void getCalendarAsync(@Suspended AsyncResponse asyncResponse);
    
    @GET
    @Produces({ MediaType.APPLICATION_JSON})
    @Path("/greet")
    public Greeting sayGreet();
     
    @POST
    @Produces({ MediaType.APPLICATION_JSON})
    @Consumes({ MediaType.APPLICATION_JSON})
    @Path("/greet")
    public Greeting createGreet(Greeting greeting);

}
