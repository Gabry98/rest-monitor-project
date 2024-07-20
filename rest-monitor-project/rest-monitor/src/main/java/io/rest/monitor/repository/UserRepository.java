package io.rest.monitor.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.rest.monitor.entity.UserEntity;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserRepository implements PanacheRepositoryBase<UserEntity, Integer>{
	
	private UserRepository() {}

}
