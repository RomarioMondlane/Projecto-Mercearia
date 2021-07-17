package com.example.Mercearia;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Controller;

@Controller
public interface ControladorPatrimonio extends JpaRepository<Patrimonio, String> {

	
 Patrimonio	findByCodigo(int codigo);
}
