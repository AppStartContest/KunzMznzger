package com.ltei.kunzmznzger.libs.time;

import com.ltei.kunzmznzger.libs.Helpers;

import org.joda.time.DateTime;

public class Date {
    private DateTime dateTime;

    public Date(DateTime dateTime) {
        this.dateTime = dateTime;
    }

    public static Date of(DateTime dateTime){
        return new Date(dateTime);
    }

    public Date plusDays(int days){
        return Date.of(this.dateTime.plusDays(days));
    }

    public Date plusMonths(int months){
        return Date.of(this.dateTime.plusMonths(months));
    }

    public Date plusYears(int years){
        return Date.of(this.dateTime.plusYears(years));
    }

    public Date minusDays(int days){
        return this.plusDays(-days);
    }

    public Date minusMonths(int months){
        return this.plusMonths(-months);
    }

    public Date minusYears(int years){
        return this.plusYears(-years);
    }

    public DateTime toDateTime(){
        return this.dateTime;
    }

    @Override
    public String toString() {
        return this.dateTime.toString(Helpers.getDateFormatter());
    }
}
