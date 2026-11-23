package com.example.demo.web;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
public record ReporteUpdateRequest(@NotBlank String estado,String responsable,String solucion,String materiales,LocalDateTime programadoPara){}
