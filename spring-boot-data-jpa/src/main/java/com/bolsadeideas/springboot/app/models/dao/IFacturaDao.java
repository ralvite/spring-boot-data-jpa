package com.bolsadeideas.springboot.app.models.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.bolsadeideas.springboot.app.models.entity.Factura;

public interface IFacturaDao extends CrudRepository<Factura, Long> {
    // CrudRepository da acceso a los métodos CRUD habituales: save(),etc.

    // se crea un nuevo método para recuperar todas las consultas
    // (factura, cliente, lineas pedido, ....) con joins
    // se evitan los LAZY


    // pasamos el atributo que relaciona (f.cliente)
    @Query("select f from Factura f join fetch f.cliente c join fetch f.items l join fetch l.producto where f.id=?1")
    public Factura  fetchByIdWithClienteWithItemFacturaWithProducto(Long id);
}
