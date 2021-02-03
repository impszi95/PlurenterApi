package com.Home.Plurenter.Service;

import com.Home.Plurenter.Model.Landlord.Landlord;
import com.Home.Plurenter.Model.MinRentTime;
import com.Home.Plurenter.Model.Rent;
import com.Home.Plurenter.Model.Tenant.Tenant;
import org.springframework.stereotype.Service;

@Service
public class Valider {
    public boolean ValidTenantDatas(Tenant tenant){
        MinRentTime minRentTime = tenant.getMinRentTime();
        if (minRentTime.getYear()==0 && minRentTime.getMonth()==0 && minRentTime.getDay()==0){
            return false;
        }
        return true;
    }
    public boolean ValidLandlordDatas(Landlord landlord){
        MinRentTime minRentTime = landlord.getMinRentTime();
        if (minRentTime.getYear()==0 && minRentTime.getMonth()==0 && minRentTime.getDay()==0){
            return false;
        }
        Rent rent = landlord.getRent();
        if (rent.getAmount()==0 || rent.getCurrency().equals("") || rent.getPeriod().equals("")){
            return false;
        }
        if (rent.getAmount()<0){
            return false;
        }
        return true;
    }
}
