package com.example.crudclinica.repository;

import com.example.crudclinica.model.TipoExame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoExameRepository extends JpaRepository<TipoExame, Long> {
}