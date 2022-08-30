package com.app.api.bdr.simulacao.repositories;

import com.app.api.bdr.simulacao.entities.SimulaInvestimentoBdr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SimulaInvestimentoBdrRepository extends JpaRepository<SimulaInvestimentoBdr, Long> {
}
