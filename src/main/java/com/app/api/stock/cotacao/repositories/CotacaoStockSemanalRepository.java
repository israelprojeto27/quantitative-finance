package com.app.api.stock.cotacao.repositories;

import com.app.api.acao.cotacao.entities.CotacaoAcaoSemanal;
import com.app.api.acao.principal.entity.Acao;
import com.app.api.stock.cotacao.entities.CotacaoStockSemanal;
import com.app.api.stock.principal.entity.Stock;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CotacaoStockSemanalRepository extends JpaRepository<CotacaoStockSemanal, Long> {

    List<CotacaoStockSemanal> findByStock(Stock stock);

    List<CotacaoStockSemanal> findByStock(Stock stock, Sort sort);

    List<CotacaoStockSemanal> findByData(LocalDate dt);

    List<CotacaoStockSemanal> findByStockAndData(Stock stock, LocalDate dataCotacao);
}
