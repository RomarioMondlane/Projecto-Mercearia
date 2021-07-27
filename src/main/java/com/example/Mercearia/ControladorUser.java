package com.example.Mercearia;



import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface ControladorUser extends JpaRepository<User, String> {
    	
	public User findByusername(String username);
	public User findBycodigo(int codigo);
}
