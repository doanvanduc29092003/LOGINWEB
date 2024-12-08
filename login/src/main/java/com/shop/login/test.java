package com.shop.login;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class test {
	public static void main(String[] args) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println(encoder.encode("admin")); // Sử dụng mật khẩu "admin"
        //System.out.println(encoder.encode("khach")); 
        
	}
}
