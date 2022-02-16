package com.joel.demoboot.service;

import java.util.List;
import java.util.Optional;

import com.joel.demoboot.modelo.User;

public interface UserService {
	
	public Optional<User> findById(Long id);
	
	public User findByName(String name);
	
	public void saveUser(User user);
	
	public void updateUser(User user);
	
	public void deleteUserById(Long id);
	
	public List<User> findAllUsers();
	
	public void deleteAllUsers();
	
	public Boolean isUserExist(User user);
}
