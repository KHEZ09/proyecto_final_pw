package com.example.demo.repository;
import com.example.demo.model.Reporte;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface ReporteRepository extends JpaRepository<Reporte,Long>{ List<Reporte> findByCreadoPorOrderByCreadoEnDesc(String creadoPor); List<Reporte> findByResponsableOrderByCreadoEnDesc(String responsable); Optional<Reporte> findByCodigo(String codigo); }
