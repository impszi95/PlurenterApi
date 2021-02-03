package com.Home.Plurenter.Repo;

import com.Home.Plurenter.Model.Landlord.ActiveLandlord;
import com.Home.Plurenter.Model.Tenant.ActiveTenant;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ActiveTenantRepo extends MongoRepository<ActiveTenant, String> {
}
