package com.url.dtos;

import java.util.Set;

import lombok.Data;

@Data
public class LoginRequest {
	private String username;
	private String password;
}
