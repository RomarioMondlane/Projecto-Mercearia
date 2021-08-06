package com.example.Mercearia;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Service;

@Service
public class DetalhesUser implements UserDetailsService{

   @Autowired
	private	final ControladorUser userR;
	
  

	public DetalhesUser(ControladorUser userR) {
		this.userR = userR;
		
	}
	
	







	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User u=userR.findByusername(username);
		if(u==null) {
			
			throw new UsernameNotFoundException("Usuario nao encontrado");
		}

		UserPrincipal p=new UserPrincipal(u);
		List<GrantedAuthority> rolesUser= AuthorityUtils.createAuthorityList("User");
		List<GrantedAuthority> rolesAdmin= AuthorityUtils.createAuthorityList("Admin");
		return new org.springframework.security.core.userdetails.User(p.getUsername(),p.getPassword(),u.isadmin?rolesAdmin:rolesUser);
	 
	}


	

}
