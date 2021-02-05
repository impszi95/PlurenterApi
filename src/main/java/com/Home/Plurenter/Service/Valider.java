package com.Home.Plurenter.Service;

import com.Home.Plurenter.Model.Landlord.Landlord;
import com.Home.Plurenter.Model.MinRentTime;
import com.Home.Plurenter.Model.Rent;
import com.Home.Plurenter.Model.Tenant.Tenant;
import com.Home.Plurenter.Model.User;
import com.Home.Plurenter.Security.Payload.Request.SignupRequest;
import org.springframework.stereotype.Service;

@Service
public class Valider {
    public boolean ValidUserDatas(User user){
        if (user.getName().equals("")){
            return false;
        }
        return true;
    }
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
    public boolean ValidSignUp(SignupRequest signupRequest){
        if (signupRequest.getEmail() == null ||
                signupRequest.getName() == null ||
            signupRequest.getPassword() == null ||
                signupRequest.getType() == null ||
                signupRequest.getPassword().length()<8 ||
                (!signupRequest.getType().equals("tenant") && !signupRequest.getType().equals("landlord"))||
                !signupRequest.isTerms()
        ){
            return false;
        }
        return true;
    }
}
