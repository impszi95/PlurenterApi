package com.Home.Tinder.Repo;

import com.Home.Tinder.Model.Photo;
import com.Home.Tinder.Model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface PhotoRepository extends MongoRepository<Photo, String> {
    List<Photo> findByUserId(String userId);
}
