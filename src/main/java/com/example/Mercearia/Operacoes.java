package com.example.Mercearia;



  
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@RestController

public class Operacoes    {
	
	@Autowired    
	private Controlador c;
	@Autowired
	private ControladorPatrimonio patri;
	@Autowired
	private ControladorUser us;
	
	
	
	@GetMapping("/")
	@Transactional
	
	public boolean login(@AuthenticationPrincipal UserDetails detalhes) {
	
		return SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
		

	}
	@GetMapping("/codigo")
	@Transactional
	
	public int codigo(@AuthenticationPrincipal UserDetails detalhes) {
		
		return us.findByusername(detalhes.getUsername()).getCodigo();
		

	}
	

	
	
	@RequestMapping(value="/salvar", method=RequestMethod.POST)
	@Transactional
//	@PreAuthorize("hasRole('User')")
	@PreAuthorize("hasAuthority('Admin')")
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public String form(@Valid  @ModelAttribute Produto e,@AuthenticationPrincipal UserDetails detalhes,@RequestParam("file") MultipartFile file) throws IOException {
		float deposito=e.quantidade*e.preco;
		
		
		
		
		if(c.findByNome(e.getNome()).size()==0) {
			
			
			Produto p=new Produto(e.getCategoria(),e.getNome(),e.getPreco(), e.getQuantidade(),compressBytes(file.getBytes()),e.getDescricao());
			c.save(p);
			return "Salvo com sucesso!";	
		}
		
		return "Registo Negado, Pois Produto ja esta registado registado";
		
				
	}
	
	@RequestMapping(value = "/imagemProduto", method = RequestMethod.POST)
	@Transactional
	public String Defnirsubmit(@RequestParam("file") MultipartFile file,@RequestParam("id") int id ) throws IOException {
		String destino="/imgProduto/"+file.getOriginalFilename();
		        File f=new File(destino);
		        Produto pp=c.findByCodigo(id);
		      
		
		return "Foto actualizada com sucesso!";
	}
	
	

	@RequestMapping(value="/criar", method=RequestMethod.POST)	
	@Transactional
	@PreAuthorize("hasAuthority('Admin')")
	public String CriarUser(@Valid  @ModelAttribute User e,@RequestParam("file") MultipartFile file) throws IllegalStateException, IOException {
		BCryptPasswordEncoder pass=new BCryptPasswordEncoder();
		User u=new User(e.getNome(),e.getUsername(),pass.encode(e.password),compressBytes(file.getBytes()),e.isadmin);
		
		if(us.findByusername(u.getUsername())!=null ) {
			
			return "Usuario exsitente!";
			
		}else {
			us.save(u);}
		return "Usuario criado com sucesso!";
		
	
	}
	
	@Bean(name = "multipartResolver")
	public CommonsMultipartResolver multipartResolver() {
	    CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
	    //multipartResolver.setMaxUploadSize(100000);
	    return multipartResolver;
	}
	
	@RequestMapping(value = "/definir", method = RequestMethod.POST)
	@Transactional
	public String submit(@RequestParam("file") MultipartFile file,@RequestParam("id") int id ) throws IOException {
		String fich=StringUtils.cleanPath(file.getOriginalFilename());
		String destino="/img/"+file.getOriginalFilename();
		
		        File f=new File(destino);
		        User user=us.findBycodigo(id);
		       user.setPicByte(file.getBytes());
		       us.save(user);
		        file.transferTo(f);
		
		return "Foto actualizada com sucesso!";
	}
	
	
	
	
	//@RequestMapping(value="/listar", method=RequestMethod.GET)
	
	//public Page<Produto> lista(@PageableDefault(page=0,size=4, sort="nome", direction=Direction.ASC ) Pageable pagee) {
		
		//return  c.findAll(pagee);
		
	//}
	
	@RequestMapping(value="/listar", method=RequestMethod.GET)
	
	public List<Produto> lista() {
//@PageableDefault(page=0,size=4, sort="nome", direction=Direction.ASC ) Pageable pagee
		List<Produto>  lista=(List<Produto>) c.findAll();
		
		for(int i=0;i<lista.size();i++) {
			;
			lista.get(i).setPicByte(decompressBytes(lista.get(i).getPicByte()));
		}
		
	return  lista;
	

		
	}
	
	@RequestMapping(value="/listarUser", method=RequestMethod.GET)

	public List<User> listaUser() {
		List<User>  lista=us.findAll();
		
		for(int i=0;i<lista.size();i++) {
			;
			lista.get(i).setPicByte(decompressBytes(lista.get(i).getPicByte()));
		}
		
	return  lista;
	
}
	

	@RequestMapping(value="/listarUserUnico", method=RequestMethod.GET)

	public User listaUserUnico(@RequestParam("id") int id) {
		User user=us.findBycodigo(id);
		
		
	  		user.setPicByte(decompressBytes(user.getPicByte()))		;
	  		return user;
	
}


@RequestMapping(value="/listarCategoria", method=RequestMethod.GET)
	
	public List<String> listaCategoria() {
	List<String> k=new ArrayList<String>();
	
	for(int i=0;i<c.findAll().size();i++) {

			k.add(i,c.findAll().get(i).getCategoria());
	}
	return k;  
		
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
	
	
	
	@RequestMapping(value="/compra", method=RequestMethod.POST)
	@Transactional
	public String Compra( @RequestParam("codigo") int codigo,@RequestParam("quant") int quant) {
		Produto k=c.findByCodigo(codigo);
		System.out.println(k.toString());
		if(k.getQuantidade()>=quant) {
			
		int nova=k.getQuantidade()-quant;
		k.setQuantidade(nova);
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
	
	@RequestMapping(value="/deleteUser", method=RequestMethod.GET)
	@Transactional
	public String ApagaUser( @RequestParam("codigo") int e) {
		User pr=us.findBycodigo(e);
			us.delete(pr);		
		return " Usuario com sucesso";
	}
	
	
	
	public static byte[] compressBytes(byte[] data) {
		Deflater deflater = new Deflater();
		deflater.setInput(data);
		deflater.finish();
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
		byte[] buffer = new byte[40000000];
		while (!deflater.finished()) {
			int count = deflater.deflate(buffer);
			outputStream.write(buffer, 0, count);
		}
		try {
			outputStream.close();
		} catch (IOException e) {
		}
		//System.out.println("Compressed Image Byte Size - " + outputStream.toByteArray().length);
		return outputStream.toByteArray();
	}
	// uncompress the image bytes before returning it to the angular application
	public static byte[] decompressBytes(byte[] data) {
		Inflater inflater = new Inflater();
		inflater.setInput(data);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
		byte[] buffer = new byte[40000000];
		try {
			while (!inflater.finished()) {
				int count = inflater.inflate(buffer);
				outputStream.write(buffer, 0, count);
			}
			outputStream.close();
		} catch (IOException ioe) {
		} catch (DataFormatException e) {
		}
		return outputStream.toByteArray();
	}

	
	
}
