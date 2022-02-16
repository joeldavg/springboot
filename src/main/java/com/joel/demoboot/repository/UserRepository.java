package com.joel.demoboot.repository;

import org.springframework.data.repository.CrudRepository;

import com.joel.demoboot.modelo.User;

public interface UserRepository extends CrudRepository<User, Long> {
	
	public User findByUsername(String username);
	
}
