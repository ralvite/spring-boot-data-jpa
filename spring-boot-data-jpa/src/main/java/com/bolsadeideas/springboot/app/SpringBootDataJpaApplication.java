package com.bolsadeideas.springboot.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.bolsadeideas.springboot.app.models.service.IUploadFileService;

@SpringBootApplication
public class SpringBootDataJpaApplication implements CommandLineRunner {
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    IUploadFileService uploadFileService;

	public static void main(String[] args) {
		SpringApplication.run(SpringBootDataJpaApplication.class, args);
	}

    @Override
    public void run(String... args) throws Exception {
        // crea programaticamente la carpeta 'uploads'
        // cada vez que se levanta el proyecto
        // se debe modificar el service IUploadFileService
        uploadFileService.deleteAll();
        uploadFileService.init();

        // generamos las contraseñas a partir del passw de ejemplo
        String password = "123456";
        // crear 2 claves encriptadas (permite generar varias encript para el mismo string)
        for (int index = 0; index < 2; index++) {
            String bcryptPassword = passwordEncoder.encode(password);
            // print console
            System.out.println(bcryptPassword);
            // pego desde las encriptaciones que aparecen en consola
            // $2a$10$9MfoZ.gI93BWAA/OHmzfKudnZ9/CjaPShVYgRnywct7JZiQoF.bdm
            // $2a$10$THnLKN.HudN2/d7RI0p4O.OPCvsWZlxPL3QJIKS9qUUJoNGcJWOoa
            // la primera la uso para ROLE_ADMIN y la segunda para ROLE_USER
        }

    }

}
