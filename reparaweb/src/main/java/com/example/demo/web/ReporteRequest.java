package com.example.demo.web;
import jakarta.validation.constraints.*;
public record ReporteRequest(@NotBlank @Size(max=120) String titulo,@NotBlank @Size(max=1000) String descripcion,@NotBlank String categoria,@NotBlank @Size(max=120) String ubicacion,@NotBlank String urgencia,@Size(max=500) String fotoUrl){}
