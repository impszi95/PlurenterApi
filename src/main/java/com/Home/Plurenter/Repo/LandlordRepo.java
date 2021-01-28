package com.Home.Plurenter.Repo;

import com.Home.Plurenter.Model.Landlord;
import com.Home.Plurenter.Model.Tenant;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LandlordRepo extends MongoRepository<Landlord, String> {
}