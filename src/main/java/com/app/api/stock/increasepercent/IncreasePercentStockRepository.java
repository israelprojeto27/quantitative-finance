package com.app.api.stock.increasepercent;

import com.app.api.acao.enums.PeriodoEnum;
import com.app.api.stock.principal.entity.Stock;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncreasePercentStockRepository extends JpaRepository<IncreasePercentStock, Long> {

    long deleteByPeriodo(PeriodoEnum periodo);

    List<IncreasePercentStock> findByStockAndPeriodo(Stock stock, PeriodoEnum periodo);

    List<IncreasePercentStock> findByStockAndPeriodo(Stock stock, PeriodoEnum periodo, Sort sort);
}
