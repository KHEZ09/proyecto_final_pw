package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity @Table(name="reportes")
public class Reporte {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @Column(nullable=false, unique=true, length=20) private String codigo;
    @Column(nullable=false, length=120) private String titulo;
    @Column(nullable=false, length=1000) private String descripcion;
    @Column(nullable=false, length=40) private String categoria;
    @Column(nullable=false, length=120) private String ubicacion;
    @Column(nullable=false, length=15) private String urgencia;
    @Column(nullable=false, length=20) private String estado;
    @Column(length=500) private String fotoUrl;
    @Column(nullable=false, length=50) private String creadoPor;
    @Column(length=50) private String responsable;
    @Column(length=500) private String solucion;
    @Column(length=500) private String materiales;
    private LocalDateTime programadoPara;
    @Column(nullable=false) private LocalDateTime creadoEn;
    private LocalDateTime actualizadoEn;
    public Reporte(){ }
    public Long getId(){return id;} public String getCodigo(){return codigo;} public void setCodigo(String v){codigo=v;}
    public String getTitulo(){return titulo;} public void setTitulo(String v){titulo=v;} public String getDescripcion(){return descripcion;} public void setDescripcion(String v){descripcion=v;}
    public String getCategoria(){return categoria;} public void setCategoria(String v){categoria=v;} public String getUbicacion(){return ubicacion;} public void setUbicacion(String v){ubicacion=v;}
    public String getUrgencia(){return urgencia;} public void setUrgencia(String v){urgencia=v;} public String getEstado(){return estado;} public void setEstado(String v){estado=v;}
    public String getFotoUrl(){return fotoUrl;} public void setFotoUrl(String v){fotoUrl=v;} public String getCreadoPor(){return creadoPor;} public void setCreadoPor(String v){creadoPor=v;}
    public String getResponsable(){return responsable;} public void setResponsable(String v){responsable=v;} public String getSolucion(){return solucion;} public void setSolucion(String v){solucion=v;}
    public String getMateriales(){return materiales;} public void setMateriales(String v){materiales=v;} public LocalDateTime getProgramadoPara(){return programadoPara;} public void setProgramadoPara(LocalDateTime v){programadoPara=v;}
    public LocalDateTime getCreadoEn(){return creadoEn;} public void setCreadoEn(LocalDateTime v){creadoEn=v;} public LocalDateTime getActualizadoEn(){return actualizadoEn;} public void setActualizadoEn(LocalDateTime v){actualizadoEn=v;}
}
