package com.app.api.bdr.simulacao.repositories;

import com.app.api.bdr.simulacao.entities.SimulaDetailInvestimentoBdr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SimulaDetailInvestimentoBdrRepository extends JpaRepository<SimulaDetailInvestimentoBdr, Long> {

    Optional<SimulaDetailInvestimentoBdr> findBySigla(String sigla);
}
