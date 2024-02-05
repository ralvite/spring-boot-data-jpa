package com.bolsadeideas.springboot.app.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.bolsadeideas.springboot.app.models.entity.Usuario;

public interface IUsuarioDao extends CrudRepository<Usuario, Long> {
    
    // con CRUDREPOSITORY se facilita la consulta a la base de datos
    // sería equivalente a
    // usersByUsernameQuery("select username, password, enabled from users where username=?
    public Usuario findByUsername(String username);
}
