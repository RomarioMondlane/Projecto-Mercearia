package com.example.Mercearia;



  
import org.springframework.transaction.annotation.Transactional;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Base64;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@RestController
public class Operacoes  {
	
	@Autowired    
	private Controlador c;
	@Autowired
	private ControladorPatrimonio patri;
	@Autowired
	private ControladorUser us;
	
	
	
	@RequestMapping(value="/login")
	@Transactional
	
	public boolean login(@RequestBody User user) {
		boolean result=false;
		System.out.println(user.username+" "+user.password);
		User u=us.findByusername(user.username);
		
		if(u!=null && u.getPassword().equals(user.password)) {
				result=true;
		}

		return result;
	}
	
	
	@RequestMapping(value="/salvar", method=RequestMethod.POST)
	@Transactional
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public String form(@Valid  @RequestBody Produto e,@AuthenticationPrincipal UserDetails detalhes) {
		float deposito=e.quantidade*e.preco;
		
		Patrimonio	pp=patri.findByCodigo(1);
		
		
		if(c.findByNome(e.getNome()).size()==0) {
			
			pp.setValorAcomulado(pp.getValorAcomulado()+deposito);
			patri.save(pp);
			c.save(e);
			return "Salvo com sucesso!";	
		}
		
		return "Registo Negado, Pois Produto ja esta registado registado";
		
				
	}
	

	@RequestMapping(value="/criar", method=RequestMethod.POST)	
	@Transactional
	
	public void CriarUser(@Valid  @RequestBody User e) {
		BCryptPasswordEncoder pass=new BCryptPasswordEncoder();
		
		User u=new User(e.getNome(),e.getUsername(),pass.encode(e.password),e.isadmin);
	
		us.save(u);
		
	
	}
	
	@RequestMapping(value="/listar", method=RequestMethod.GET)
	
	public List<Produto> lista( ) {
		
		return (List<Produto>) c.findAll();
		
	}
	
	@RequestMapping(value="/patrimonio", method=RequestMethod.GET)
	public List<Patrimonio> patrimonio() {
		return (List<Patrimonio>) patri.findAll();
			
		
		
	}
	
	@RequestMapping("/user")
    public Principal user(HttpServletRequest request) {
        String authToken = request.getHeader("Authorization")
          .substring("Basic".length()).trim();
        return () ->  new String(Base64.getDecoder()
          .decode(authToken)).split(":")[0];
    }
	
	
	
	@RequestMapping(value="/compra", method=RequestMethod.GET)
	@Transactional
	public String Compra( @RequestBody Produto e) {
		Produto k=c.findByCodigo(e.codigo);
		Patrimonio pt=patri.findByCodigo(1);
		
		
		
		
		if(k.getQuantidade()>=e.getQuantidade()) {
			
			int nova=k.getQuantidade()-e.getQuantidade();
		k.setQuantidade(nova);
		pt.setValorDepostidado(k.preco*e.quantidade+pt.getValorDepostidado());
		patri.save(pt);
		c.save(k);
		}else {
			return "Quantidade indisponivel";
			
		}
		
		return "Operacao efectuada com sucesso!";
	}
	
	@RequestMapping(value="/delete", method=RequestMethod.GET)
	@Transactional
	public String Apaga( @RequestParam("codigo") int e) {
		Produto pr=c.findByCodigo(e);
			c.delete(pr);		
		
		return "Produto apagado com sucesso";
	}
	
	
	
}
