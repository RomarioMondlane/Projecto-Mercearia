package com.example.Mercearia;


import javax.sql.DataSource;



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.*;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

@Configuration 
public class Configuracao {
	
	@Bean
	public DataSource data() {
		
		DriverManagerDataSource dataS= new DriverManagerDataSource();
		
		dataS.setDriverClassName("com.mysql.cj.jdbc.Driver");
		dataS.setUrl("jdbc:mysql://localhost:3306/mercearia");
		dataS.setUsername("root");
		dataS.setPassword("");
		return dataS;
		
	}
	
	@Bean
	public JpaVendorAdapter jpa() {
		HibernateJpaVendorAdapter adapter=new HibernateJpaVendorAdapter();
		adapter.setDatabase(Database.MYSQL);
		adapter.setShowSql(true);
		adapter.setGenerateDdl(true);
		adapter.setDatabasePlatform("org.hibernate.dialect.MySQL5Dialect");
		adapter.setPrepareConnection(true);
		return adapter;
	
	}
	@Bean
	public User UserBean() {
		
		return new User();
	}
	
	
	
	
	
}