package com.example.Mercearia;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

import com.sun.istack.NotNull;


@Entity 
public class Produto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public int codigo;
	@NotBlank 
	public String categoria;
	@NotBlank 
	@Column(unique = true)
	public String nome;
	@NotNull 
	public float preco;
	@NotNull
	public int quantidade;

	@Column(name = "picByte", length = 40000000)
	private byte[] picByte;
	
	public String descricao;
	
	

	public Produto() {
		super();
	}



	public Produto(@NotBlank String categoria, @NotBlank String nome, float preco, int quantidade, byte[] picByte,String descricao) {
		super();
		this.categoria = categoria;
		this.nome = nome;
		this.preco = preco;
		this.quantidade = quantidade;
		this.picByte = picByte;
		this.descricao=descricao;	}



	public int getCodigo() {
		return codigo;
	}



	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}



	public String getCategoria() {
		return categoria;
	}



	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}



	public String getNome() {
		return nome;
	}



	public void setNome(String nome) {
		this.nome = nome;
	}



	public float getPreco() {
		return preco;
	}



	public void setPreco(float preco) {
		this.preco = preco;
	}



	public int getQuantidade() {
		return quantidade;
	}



	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}



	public byte[] getPicByte() {
		return picByte;
	}



	public void setPicByte(byte[] picByte) {
		this.picByte = picByte;
	}



	public String getDescricao() {
		return descricao;
	}



	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	
	
	
		
	
}
