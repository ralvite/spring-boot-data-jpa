package com.bolsadeideas.springboot.app.models.dao;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.bolsadeideas.springboot.app.models.entity.Producto;
import java.util.List;


public interface IProductoDao extends CrudRepository<Producto, Long> {
    // implementamos un método CRUD que devuelva resultados
    // de una consulta 'WHERE LIKE ...'
    // el select es a nivel objeto no tabla de bd
    // ?1 : param 1 = term
    @Query("select p from Producto p where p.nombre like %?1%")
    public List<Producto> findByNombre(String term);

    // la siguiente consulta realiza la misma consulta
    // que la anterior pero case insensitive por el nombre estandard "IgnoreCase"
    public List<Producto> findByNombreLikeIgnoreCase(String term);
}
