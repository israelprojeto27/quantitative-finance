package com.app.api.stock.principal;

import com.app.api.stock.principal.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {

    Optional<Stock> findBySigla(String sigla);

    List<Stock> findBySiglaContaining(String sigla);

}
