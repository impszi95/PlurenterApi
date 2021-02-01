package com.Home.Plurenter.Model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Rent {
    private int amount;
    private String currency;
    private String period;

    public String toString(){
        String result = amount+" "+currency + " / ";
        switch (period){
            case "Yearly":
                result+="year";
                break;
            case "Monthly":
                result +="month";
                break;
            case "Weekly":
                result  +="week";
                break;
            case "Daily":
                result +="day";
                break;
        }
        return result;
    }
}
