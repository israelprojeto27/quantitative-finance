package com.app.api.stock.simulacao.repositories;

import com.app.api.stock.simulacao.entities.SimulaDetailInvestimentoStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SimulaDetailInvestimentoStockRepository extends JpaRepository<SimulaDetailInvestimentoStock, Long> {

    Optional<SimulaDetailInvestimentoStock> findBySigla(String sigla);
}
