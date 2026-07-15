package com.project.producto_service.model;

import jakarta.persistence.*;

@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 100)
    private String titulo;

    @Column(nullable = false)
    private Double precio;

    @Column
    private Integer duracionHoras;

    @Column(name = "categoria_id")
    private Integer categoriaId;

    @Column
    private Boolean activo = true;

    public Producto() {
    }

    public Producto(Integer id, String titulo, Double precio, Integer duracionHoras, Integer categoriaId, Boolean activo) {
        this.id = id;
        this.titulo = titulo;
        this.precio = precio;
        this.duracionHoras = duracionHoras;
        this.categoriaId = categoriaId;
        this.activo = activo;
    }

    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }
    public Integer getDuracionHoras() { return duracionHoras; }
    public void setDuracionHoras(Integer duracionHoras) { this.duracionHoras = duracionHoras; }
    public Integer getCategoriaId() { return categoriaId; }
    public void setCategoriaId(Integer categoriaId) { this.categoriaId = categoriaId; }
    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
}