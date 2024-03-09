package com.bolsadeideas.springboot.app.view.json;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.bolsadeideas.springboot.app.models.entity.Cliente;

@Component("listar.json")
@SuppressWarnings("unchecked")
public class ClienteListJsonView extends MappingJackson2JsonView {

    @Override
    protected Object filterModel(Map<String, Object> model) {
        // permite quitar elementos del modelo que pasamos a la vista (paginador, titulo, etc)
        model.remove("titulo");
        model.remove("page");

        // se obtiene el cliente y se elimina del modelo el paginable
        Page<Cliente> clientes = (Page<Cliente>) model.get("clientes");
		model.remove("clientes");
		
        // se obtiene el listado de clientes a través del contect del paginable
		model.put("clientes", clientes.getContent());
        
        return super.filterModel(model);
    }
    // en el caso de JSON al serializar nativamente los arrays, etc
    // no necesita de un wrapper como en el caso de xml

}



