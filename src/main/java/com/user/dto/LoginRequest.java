package com.user.dto;

import lombok.*;

@Data
@NoArgsConstructor
public class LoginRequest {
	private String email;
    private String password;
}
