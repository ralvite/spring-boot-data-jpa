package com.bolsadeideas.springboot.app.models.service;


import java.io.IOException;
import java.net.MalformedURLException;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface IUploadFileService {

	public Resource load(String filename) throws MalformedURLException;

	public String copy(MultipartFile file) throws IOException;

	public boolean delete(String filename);

    // borra todas las imágenes cuando se levanta el servidor
    public void deleteAll();

    // crea el directorio 'uplods'
    public void init() throws IOException;

}
