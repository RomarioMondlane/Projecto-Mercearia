package com.example.Mercearia;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ConfiguracaoSeguranca extends WebSecurityConfigurerAdapter {
	
	@Autowired	
	private DetalhesUser us;
	//UserDetailsService us;	
	
	

	protected void configure(HttpSecurity http) throws Exception {
		
		
	        http.csrf().disable()
	          .authorizeRequests()
	          .antMatchers("/login").permitAll()
	          .anyRequest()
	          .authenticated()
	          .and()
	          .httpBasic();
	    }		
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
	
		auth.userDetailsService(us).passwordEncoder(new BCryptPasswordEncoder());
		
	}
	
	
	
	
}
