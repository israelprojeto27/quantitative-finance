package com.app.api.stock.analise;

import com.app.api.stock.analise.entities.StockAnalise;
import com.app.api.stock.principal.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StockAnaliseRepository extends JpaRepository<StockAnalise, Long> {

    Optional<StockAnalise> findByStock(Stock stock);
}
