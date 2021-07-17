package com.example.Mercearia;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Encry {
public static void main(String[]args) {
	BCryptPasswordEncoder pass=new BCryptPasswordEncoder();
	System.out.println(pass.encode("1234")); 
	System.out.println("ddd"); 
	
	;
	
	
}
}
