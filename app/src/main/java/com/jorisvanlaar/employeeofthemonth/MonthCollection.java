package com.jorisvanlaar.employeeofthemonth;

public class MonthCollection {

    private String [] months;

    public MonthCollection() {
        this.months = new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    }

    public String getMonth(int month) {
        return months[month];
    }
}
