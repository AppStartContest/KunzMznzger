package com.ltei.kunzmznzger.libs.time;

import com.ltei.kunzmznzger.libs.Helpers;

import org.joda.time.DateTime;

public class Time {

    private DateTime dateTime;

    public Time(DateTime dateTime) {
        this.dateTime = dateTime;
    }

    public static Time of(DateTime dateTime){
        return new Time(dateTime);
    }

    public DateTime toDateTime(){
        return this.dateTime;
    }

    public Time plusSeconds(int sec){
        return Time.of(dateTime.plusSeconds(sec));
    }

    public Time plusMinutes(int min){
        return Time.of(dateTime.plusMinutes(min));
    }

    public Time plusHours(int h){
        return Time.of(dateTime.plusHours(h));
    }

    public Time minusSeconds(int sec){
        return this.plusSeconds(-sec);
    }

    public Time minusMinutes(int min){
        return this.plusMinutes(-min);
    }

    public Time minusHours(int h){
        return this.plusHours(-h);
    }

    @Override
    public String toString() {
        return this.dateTime.toString(Helpers.getTimeFormatter());
    }
}
