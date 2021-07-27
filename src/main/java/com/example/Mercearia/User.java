package com.example.Mercearia;



import java.io.Serializable;

import javax.persistence.Column;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class User implements Serializable{
	
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public int codigo;
	@NotBlank
	@Column(unique = true)
	public String nome;
	@NotBlank
	@Column(unique = true)
	public String username;
	@NotEmpty
	@JsonIgnore
	public String password;
	
	public boolean isadmin;
	 private String file;
	
	public User() {}
	
	
	public User(int codigo, @NotBlank String nome, @NotBlank String username, @NotEmpty String password,
			@NotEmpty boolean isAdmin) {
		
		this.codigo = codigo;
		this.nome = nome;
		this.username = username;
		this.password = password;
		this.isadmin = isAdmin;
	}

	


	public User(@NotBlank String nome, @NotBlank String username, @NotEmpty String password, boolean isadmin,
			String file) {
		super();
		this.nome = nome;
		this.username = username;
		this.password = password;
		this.isadmin = isadmin;
		this.file = file;
	}


	public int getCodigo() {
		return codigo;
	}
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isAdmin() {
		return isadmin;
	}
	public void setAdmin(boolean isAdmin) {
		this.isadmin = isAdmin;
	}


	public boolean isIsadmin() {
		return isadmin;
	}


	public void setIsadmin(boolean isadmin) {
		this.isadmin = isadmin;
	}


	public String getFile() {
		return file;
	}


	public void setFile(String file) {
		this.file = file;
	}


	


	
	
}
	