package com.app.api.stock.simulacao.repositories;

import com.app.api.stock.simulacao.entities.SimulaInvestimentoStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SimulaInvestimentoStockRepository extends JpaRepository<SimulaInvestimentoStock, Long> {
}
