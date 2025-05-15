package com.url.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.url.dtos.LoginRequest;
import com.url.dtos.RegisterRequest;
import com.url.models.User;
import com.url.service.UserService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {
	
	private UserService userService;
	
//Login User	
	@PostMapping("/public/login")
	public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest)
	{
		System.out.println("**************");
		System.out.println(loginRequest);
		return ResponseEntity.ok(userService.authenticateUser(loginRequest));
	}
	
	
//Adding User
	@PostMapping("/public/register")
	public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest)
	{
		User user=new User();
		user.setUsername(registerRequest.getUsername());
		user.setPassword(registerRequest.getPassword());
		user.setEmail(registerRequest.getEmail());
		user.setRole("ROLE_USER");
		
		userService.registerUser(user);
		return ResponseEntity.ok("User register successfully...!!!");
	}
	

	
}
