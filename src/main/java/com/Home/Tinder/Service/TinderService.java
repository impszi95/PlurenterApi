package com.Home.Tinder.Service;

import com.Home.Tinder.Model.Photo;
import com.Home.Tinder.Model.User;
import com.Home.Tinder.Repo.UserRepo;
import com.Home.Tinder.Security.Payload.Response.TinderNextUserResponse;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import io.jsonwebtoken.lang.Assert;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperationContext;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class TinderService {

    @Autowired
    UserRepo userRepo;

    public TinderNextUserResponse getNextUser() {
        //Ha userID queue<20 akk léterhozok 20nagy queue-t
        //kül peek()et-->TindernextResp-t visszaadom, aztán remove first-ölöm

        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        int n = 5; //queue size
        if (userDetails.getNextUsersQueue().size() < n){
            userDetails.setNextUsersQueue(getNDifferentUserIds(n));
        }

        String nextUserID = userDetails.getNextUsersQueue().peek();
        userDetails.getNextUsersQueue().remove();
        List<String> photos = userDetails.getPhotos();
        String photo = "";
        if (photos.size() != 0) {
            photo = userDetails.getPhotos().get(0);
        }
        return new TinderNextUserResponse(nextUserID, photo);
    }

    private Queue<String> getNDifferentUserIds(int n){
       Queue<String> userIds = new LinkedBlockingQueue<>();

       int size = userRepo.findAll().size();
       Random rand = new Random();
       int counter = 0;

       while (counter<n){
           String rndUserId = userRepo.findAll().get(rand.nextInt(size)).getId();
           if (!userIds.contains(rndUserId)){
               userIds.add(rndUserId);
               counter++;
           }
       }
       return userIds;
    }
}