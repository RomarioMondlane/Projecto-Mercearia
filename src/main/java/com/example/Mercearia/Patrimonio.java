package com.example.Mercearia;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Patrimonio implements Serializable{

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public int codigo;
	public float valordepostidado;
	public float valoracomulado;
	public Patrimonio() {
	}
	
	public Patrimonio( float valordepostidado, float valoracomulado) {
	
		this.valordepostidado = valordepostidado;
		this.valoracomulado = valoracomulado;
	}

	public float getValorDepostidado() {
		return valordepostidado;
	}
	public void setValorDepostidado(float valorDepostidado) {
		this.valordepostidado = valorDepostidado;
	}
	public float getValorAcomulado() {
		return valoracomulado;
	}
	public void setValorAcomulado(float valorAcomulado) {
		this.valoracomulado = valorAcomulado;
	}
	
	
	
}
