package com.Home.Plurenter.Repo;

import com.Home.Plurenter.Model.Landlord.ActiveLandlord;
import com.Home.Plurenter.Model.Landlord.Landlord;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ActiveLandlordRepo extends MongoRepository<ActiveLandlord, String> {
}