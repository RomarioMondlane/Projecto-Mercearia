package com.example.Mercearia;



  
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
	
	public boolean login() {
		boolean result=false;
		

		return true;
	}
	
	
	@RequestMapping(value="/salvar", method=RequestMethod.POST)
	@Transactional
//	@PreAuthorize("hasRole('User')")
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
	
	public String CriarUser(@Valid  @ModelAttribute User e) throws IllegalStateException, IOException {
		BCryptPasswordEncoder pass=new BCryptPasswordEncoder();
	
		User u=new User(e.getNome(),e.getUsername(),pass.encode(e.password),e.isadmin,"null");
		us.save(u);
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
		        user.setFile(file.getOriginalFilename())	       ;
		        us.save(user);
		        file.transferTo(f);
		
		return "Foto actualizada com sucesso!";
	}
	
	@GetMapping(value="carregar", produces=MediaType.IMAGE_PNG_VALUE)
	public  @ResponseBody byte[] getImage(@RequestParam("codigo")  int codigo) throws IOException {
		User user=us.findBycodigo(codigo);
		String destino="/img/"+user.getFile();
		File fich=new File(destino);
		return Files.readAllBytes(fich.toPath());
		}
	
	
	//@RequestMapping(value="/listar", method=RequestMethod.GET)
	
	//public Page<Produto> lista(@PageableDefault(page=0,size=4, sort="nome", direction=Direction.ASC ) Pageable pagee) {
		
		//return  c.findAll(pagee);
		
	//}
	
@RequestMapping(value="/listar", method=RequestMethod.GET)
	
	public List<Produto> lista() {
		
		return  c.findAll();
		
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
