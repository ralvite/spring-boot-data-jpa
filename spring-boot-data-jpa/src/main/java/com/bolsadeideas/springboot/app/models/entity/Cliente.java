package com.bolsadeideas.springboot.app.models.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
// import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "clientes")
public class Cliente implements Serializable {

    // Id anota como PK y como se genera sequence
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // se puede dar un nombre al atributo en la clas
    // y otro a la columna en la base de datos
    // @Column(name="nombre_cliente", length = 255, nullable = true)
    @NotEmpty 
    private String nombre;
    
    @NotEmpty
    private String apellido;
    
    @NotEmpty
    @Email
    private String email;

    // Temporal indica el formato de fecha
    @NotNull
    @Column(name = "create_at")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createAt;

    // un cliente - muchas facturas
    // al pasar mappedBy va a crear el cliente_id para
    // usar como FK
    // JsonManagedReference para que el json no entre en loop infinito
    // al mostrar las facturas del cliente por la relación cliente - factura - cliente
    @OneToMany(mappedBy = "cliente", fetch=FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Factura> facturas;

    

    public Cliente() {
        // facturas = new List<Factura>();
        facturas = new ArrayList<Factura>();
    }

    private String foto;

    // para implementar serializacion es aconsejable el id
    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public List<Factura> getFacturas() {
        return facturas;
    }

    public void setFacturas(List<Factura> facturas) {
        this.facturas = facturas;
    }

    public void addFactura(Factura factura) {
        facturas.add(factura);
    }

    // @PrePersist
    // public void prePersist(){
    //     createAt = new Date();
    // }

}
