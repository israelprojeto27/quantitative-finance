package com.app.api.stock.cotacao.repositories;

import com.app.api.stock.cotacao.entities.CotacaoStockMensal;
import com.app.api.stock.principal.entity.Stock;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface CotacaoStockMensalRepository extends JpaRepository<CotacaoStockMensal, Long> {

    List<CotacaoStockMensal> findByStock(Stock stock);

    List<CotacaoStockMensal> findByStock(Stock stock, Sort sort);

    List<CotacaoStockMensal> findByData(LocalDate dt);

    List<CotacaoStockMensal> findByStockAndData(Stock stock, LocalDate dataCotacao);
}
