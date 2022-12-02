package com.app.api.stock.cotacao.repositories;

import com.app.api.acao.principal.entity.Acao;
import com.app.api.stock.cotacao.entities.CotacaoStockDiario;
import com.app.api.stock.principal.entity.Stock;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CotacaoStockDiarioRepository extends JpaRepository<CotacaoStockDiario, Long> {

    List<CotacaoStockDiario> findByStock(Stock stock);

    List<CotacaoStockDiario> findByStock(Stock stock, Sort sort);

    List<CotacaoStockDiario> findByData(LocalDate dt);

    List<CotacaoStockDiario> findByStockAndData(Stock stock, LocalDate dataCotacao);
}
