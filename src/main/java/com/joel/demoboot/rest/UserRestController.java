package com.joel.demoboot.rest;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.joel.demoboot.modelo.User;
import com.joel.demoboot.service.UserService;

@RestController
@RequestMapping("/user")
public class UserRestController {

	@Autowired
	private UserService userService;

	// -----------Retrieve All Users ---------------------

	@GetMapping("/all")
	public ResponseEntity<List<User>> listAllUsers() {
		List<User> users = userService.findAllUsers();
		if (users.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT); // You may decide to return HttpStatus.NOT_FOUND
		} else {
			return new ResponseEntity<>(users, HttpStatus.OK);
		}
	}
	
	// -----------Retrieve Single User ---------------------

	@GetMapping("/{id}")
	public ResponseEntity<User> getUser(@PathVariable("id") Long id) {
		System.out.println("Fetching User with id " + id);
		Optional<User> user = userService.findById(id);

		return user.map(usr -> new ResponseEntity<>(usr, HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	// -----------Save Single User ---------------------
	
	@PostMapping("/save")
	public ResponseEntity<Void> createUser(@RequestBody User user, UriComponentsBuilder ucBuilder) {
		System.out.println("Creating User " + user.getUsername());
		
		if (userService.isUserExist(user)) {
			System.out.println("A User with name " + user.getUsername() + " already exist");
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		} else {
			userService.saveUser(user);
			HttpHeaders headers = new HttpHeaders();
			headers.setLocation(ucBuilder.path("/save/{id}").buildAndExpand(user.getId()).toUri());
			return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
		}
	}
	
	//------------------Update a User ---------------------------
	
	@PutMapping("/{id}")
	public ResponseEntity<User> updateUser(@PathVariable("id") Long id, @RequestBody User user) {
		System.out.println("Updating User " + id);
		
		User currentUser = userService.findById(id).map(usr -> usr).orElse(null);
		
		if (currentUser == null) {
			System.out.println("User with id " + id + "not found");
			return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
		} else {
			currentUser.setUsername(user.getUsername());
			currentUser.setAddress(user.getAddress());
			currentUser.setEmail(user.getEmail());
			userService.updateUser(currentUser);
			return new ResponseEntity<User>(currentUser, HttpStatus.OK);
		}
				
	}
	
	//------------------Delete a User ---------------------------
	
	@DeleteMapping("/{id}")
	public ResponseEntity<User> deleteUser(@PathVariable("id") Long id) {
		System.out.println("Fetching & Deleting User with id " + id);
		
		User user = userService.findById(id).map(usr -> usr).orElse(null);
		if (user == null) {
			System.out.println("Unable to delete User with id " + id + " not found");
			return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
		} else {
			userService.deleteUserById(id);
			return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
		}
	}
	
	
	//------------------Update All Users ---------------------------
	
	@DeleteMapping("/all")
	public ResponseEntity<User> deleteAllUsers() {
		System.out.println("Deleting All Users");
		
		userService.deleteAllUsers();
		
		return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
	}
	
}
