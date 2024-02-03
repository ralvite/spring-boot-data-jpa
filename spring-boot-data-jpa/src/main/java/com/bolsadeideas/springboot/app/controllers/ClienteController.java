package com.bolsadeideas.springboot.app.controllers;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
// import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// import com.bolsadeideas.springboot.app.models.dao.IClienteDao;
import com.bolsadeideas.springboot.app.models.entity.Cliente;
import com.bolsadeideas.springboot.app.models.service.IClienteService;
import com.bolsadeideas.springboot.app.models.service.IUploadFileService;
import com.bolsadeideas.springboot.app.util.paginator.PageRender;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@SessionAttributes("cliente")
public class ClienteController {

    protected final Log logger = LogFactory.getLog(this.getClass());

    // atributo del cliente DAO para realizar la consulta
    @Autowired
    // @Qualifier("clienteDaoJPA")
    // private IClienteDao clientaDao;
    // En vez del DAO, inyectamos el Service
    private IClienteService clienteService;

	@Autowired
	private IUploadFileService uploadFileService;



    @GetMapping("/index")
    public String index(Model model) {
        model.addAttribute("titulo", "Listado de clientes");
        return "index";
    }

    // para añadir varios roles usamos {}
	@Secured({"ROLE_USER","ROLE_ADMIN"})
    @GetMapping(value = "/uploads/{filename:.+}")
	public ResponseEntity<Resource> verFoto(@PathVariable String filename) {

		Resource recurso = null;

		try {
			recurso = uploadFileService.load(filename);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"")
				.body(recurso);
	}

    // @Secured("ROLE_USER")
    // se anota con preAuthorize en vez de Secured, como ejemplo
    // si fuesen varios roles sería hasAnyRole
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping(value = "/ver/{id}")
    public String ver(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash) {

        // Cliente cliente = clienteService.findOne(id);
        Cliente cliente = clienteService.fetchByIdWithFacturas(id);
        if (cliente == null) {
            flash.addFlashAttribute("error", "El cliente no existe en la base de datos");
            return "redirect:/listar";
        }

        model.put("cliente", cliente);
        model.put("titulo", "Detalle cliente: " + cliente.getNombre());
        return "ver";
    }

    // defaultvalue del paginador = 0 para la primera página
    @RequestMapping(value = {"/listar","/"}, method = RequestMethod.GET)
    public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model,
            Authentication authentication,
            HttpServletRequest request) {

        if (authentication != null) {
            // podemos pasar el nombre a una consulta, etc...
            logger.info("Hola usuario!: ".concat(authentication.getName()));
        }

        // podemos obtener el autenthication de forma estática
        // en cualquier lugar de la página con:
        // esta información de autenticación la podemos emplear para diferentes objetivos
        // aquí se usa para hacer un logger.info.
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

		if(auth != null) {
			logger.info("Utilizando forma estática SecurityContextHolder.getContext().getAuthentication(): Usuario autenticado: ".concat(auth.getName()));
		}

        if (hasRole("ROLE_ADMIN")) {
            logger.info("Hola ".concat(auth.getName()).concat( " tienes acceso!"));
        } else {
            logger.info("Hola ".concat(auth.getName()).concat( " NO tienes acceso!"));
        }

        // creamos una instancia de http request para validadr el rol
        // es necesario inyectar el objeto HttpServletRequest
        SecurityContextHolderAwareRequestWrapper securityContext = new SecurityContextHolderAwareRequestWrapper(request, "");
        if (securityContext.isUserInRole("ROLE_ADMIN")) {
            logger.info("Forma SecurityContextHolderAwareRequestWrapper. Hola usuario!: ".concat(authentication.getName()));
        } else {
            logger.info("Forma SecurityContextHolderAwareRequestWrapper. Hola ".concat(auth.getName()).concat( " NO tienes acceso!"));
        }

        // Se puede hacer lo mismo q en el bloque anterior
        // pero on el request
        if (request.isUserInRole("ROLE_ADMIN")) {
            logger.info("Forma Request. Hola usuario!: ".concat(authentication.getName()));
        } else {
            logger.info("Forma Request. Hola ".concat(auth.getName()).concat( " NO tienes acceso!"));
        }

        // crea el pageable
        Pageable pageRequest = PageRequest.of(page, 4);

        // vista paginada
        Page<Cliente> clientes = clienteService.findAll(pageRequest);

        PageRender<Cliente> pageRender = new PageRender<>("/listar", clientes);

        model.addAttribute("titulo", "Listado de clientes");
        // obtener la paginación
        model.addAttribute("clientes", clientes);
        // model.addAttribute("clientes", clienteService.findAll());
        model.addAttribute("page", pageRender);
        return "listar";
    }

    // Handler para mostrar el formulario vacío y
    // crear un cliente
    // en este caso se pasa mejor un Map<key,value>
    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/form")
    public String crear(Map<String, Object> model) {

        Cliente cliente = new Cliente();

        // en caso de Map se pasan atributos con Put
        model.put("cliente", cliente);
        model.put("titulo", "Formulario de clientes");
        return "form";

    }

    // Handler para editar los datos de un cliente existente
    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/form/{id}")
    public String editar(@PathVariable("id") Long id, Map<String, Object> model, RedirectAttributes flash) {

        // valida id
        Cliente cliente = null;

        if (id > 0) {
            cliente = clienteService.findOne(id);
            if (cliente == null) {
                flash.addAttribute("error", "El ID del usuario no existe!");
                return "redirect:/listar";
            }
        } else {
            flash.addAttribute("error", "El ID del usuario no puede ser cero!");
            return "redirect:/listar";
        }

        // en caso de Map se pasan atributos con Put
        model.put("cliente", cliente);
        model.put("titulo", "Editar cliente");
        return "form";

    }

    // Handler para manejar los datos
    // mandado en el submit del form anterior
    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/form", method = RequestMethod.POST)
    // anotar como Valid el objeto cliente
    public String guardarCliente(@Valid Cliente cliente, BindingResult result, Model model,
            @RequestParam("file") MultipartFile foto, RedirectAttributes flash,
            SessionStatus status) {

                if (result.hasErrors()) {
                    model.addAttribute("titulo", "Formulario de Cliente");
                    return "form";
                }
                
                // if (!foto.isEmpty()) {
                    
                //     if(cliente.getId() !=null 
                //             && cliente.getId() > 0
                //             && cliente.getFoto()!=null
                //             && cliente.getFoto().length() > 0) {
                        
                //         Path rootPath = Paths.get(UPLOADS_FOLDER).resolve(cliente.getFoto()).toAbsolutePath();
                //         File archivo = rootPath.toFile();
                        
                //         if(archivo.exists() && archivo.canRead()) {
                //             archivo.delete();
                //         }
                //     }

                //     // Path directorioRecursos = Paths.get("src//main//resources//static//uploads");
                //     // String rootPath = directorioRecursos.toFile().getAbsolutePath();

                //     // para usar la carpeta externa
                //     // es necesaio crear la clase de configuración McvConfig.java
                //     // String rootPath = "C://tmp//uploads";

                //     // crea un id random para cada fichero                    
                //     String uniqueFilename = UUID.randomUUID().toString() + "_" + foto.getOriginalFilename();
                //     Path rootPath = Paths.get("uploads").resolve(uniqueFilename);
                    
                //     // mapea la ruta a una carpeta 'uploads' en la raíz del proyecto
                //     Path rootAbsolutPath = rootPath.toAbsolutePath();
                    
                //     // realiza un console.log
                //     log.info("rootPath: " + rootPath);
                //     log.info("rootAbsolutPath: " + rootAbsolutPath);
        
                //     try {
                //         // byte[] bytes = foto.getBytes();
                //         // Path rutaCompleta = Paths.get(rootPath + "//" + foto.getOriginalFilename());
                //         // Files.write(rutaCompleta, bytes);
                //         // usar mejor método copy()
                //         Files.copy(foto.getInputStream(), rootAbsolutPath);
                        
                //         flash.addFlashAttribute("info", "Has subido correctamente '" + uniqueFilename + "'");
        
                //         cliente.setFoto(uniqueFilename);
        
                //     } catch (IOException e) {
                //         e.printStackTrace();
                //     }
                // }

                if (!foto.isEmpty()) {

                    if (cliente.getId() != null && cliente.getId() > 0 && cliente.getFoto() != null
                            && cliente.getFoto().length() > 0) {
        
                        uploadFileService.delete(cliente.getFoto());
                    }
        
                    String uniqueFilename = null;
                    try {
                        uniqueFilename = uploadFileService.copy(foto);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
        
                    flash.addFlashAttribute("info", "Has subido correctamente '" + uniqueFilename + "'");
        
                    cliente.setFoto(uniqueFilename);
                }
                
                String mensajeFlash = (cliente.getId() != null) ? "Cliente editado con éxito!" : "Cliente creado con éxito!";
                // guardarmos (crear/actualizar) los datos recibidos del cliente si ok
                clienteService.save(cliente);
                status.setComplete();
                flash.addFlashAttribute("success", mensajeFlash);
                return "redirect:listar";
        
    }

    // Handler para eliminar
    // usuario y ficheros de imagen en uploads
    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/eliminar/{id}")
    public String eliminar(@PathVariable("id") Long id, RedirectAttributes flash) {

        if (id > 0) {
            Cliente cliente = clienteService.findOne(id);

            clienteService.delete(id);
            flash.addAttribute("success", "Usuario eliminado correctamente!");

            // obtener la ruta abs de la imagen
			if (uploadFileService.delete(cliente.getFoto())) {
				flash.addFlashAttribute("info", "Foto " + cliente.getFoto() + " eliminada con exito!");
			}

        }

        return "redirect:/listar";

    }

	private boolean hasRole(String role) {
		
		SecurityContext context = SecurityContextHolder.getContext();
		
		if(context == null) {
            // no tiene acceso
			return false;
		}
		
		Authentication auth = context.getAuthentication();
		
		if(auth == null) {
			return false;
		}
		
        // obtiene una colección de roles=authorities
		Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
		
		
		/*
        for(GrantedAuthority authority: authorities) {
            if(role.equals(authority.getAuthority())) {
                logger.info("Hola usuario ".concat(auth.getName()).concat(" tu role es: ".concat(authority.getAuthority())));
                return true;
            }
        }
        
        return false;
		*/
		// el siguiente método retorna un boolean si contiene o no el elemento
        // en la colección
        // usando una implementación simple de rol
        return authorities.contains(new SimpleGrantedAuthority(role));
	}

}
