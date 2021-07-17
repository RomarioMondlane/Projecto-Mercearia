package com.example.Mercearia;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import org.springframework.stereotype.Controller;


@Controller
public interface Controlador extends JpaRepository<Produto, String>{

	Produto findByCodigo(int codigo);
	List<Produto> findByNome(String e);
	
	
}
