package com.Home.Plurenter.Model;

import lombok.Data;

@Data
public class MinRentTime {
    private int day;
    private int month;
    private int year;

    public MinRentTime(){
        this.year = 0;
        this.month = 0;
        this.day = 0;
    }

    public String toString(){
        String result = "";
        if (year != 0){
            result+=year;
            if (year==1){
                result+= month==0 && day==0? "year" : " year, ";
            }
            else {
                result+= month==0 && day==0? "years" : " years, ";
            }
        }
        if (month != 0){
            result+=month;
            if (month==1){
                result+= day==0? "month" : " month, ";
            }
            else {
                result+= day==0? "months" : "months, ";
            }
        }
        if (day != 0){
            result+=day;
            if (day==1){
                result+="day";
            }
            else {
                result+="days";
            }
        }
        return result;
    }
    public boolean isMoreOrEqualsThan(MinRentTime minRentTime){
        int daysSumThis = this.day + this.month*30 + this.year*365;
        int daysSumOut = minRentTime.day + minRentTime.month*30 + minRentTime.year*365;
        return daysSumThis >= daysSumOut;
    }
    public boolean isLessOrEqualsThan(MinRentTime minRentTime){
        int daysSumThis = this.day + this.month*30 + this.year*365;
        int daysSumOut = minRentTime.day + minRentTime.month*30 + minRentTime.year*365;
        return daysSumThis <= daysSumOut;
    }
}
