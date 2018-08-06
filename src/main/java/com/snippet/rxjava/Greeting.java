package com.snippet.rxjava;

import java.util.Date;

import javax.validation.constraints.Min;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonProperty;

@XmlRootElement
public class Greeting {

    @Min(value = 100)
    int id;
    @JsonProperty("changedgreeting")
    String greet;
    double sal;
    private Date datetime;

    public Greeting() {}

    public Greeting(int id, String greet, double sal) {
        this.id = id;
        this.greet = greet;
        this.sal = sal;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGreet() {
        return greet;
    }

    public void setGreet(String greet) {
        this.greet = greet;
    }

    public double getSal() {
        return sal;
    }

    public void setSal(double sal) {
        this.sal = sal;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

}