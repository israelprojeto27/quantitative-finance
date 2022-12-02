package com.app.api.stock.dividendo;

import com.app.api.stock.dividendo.entity.DividendoStock;
import com.app.api.stock.principal.entity.Stock;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DividendoStockRepository extends JpaRepository<DividendoStock, Long>  {

    List<DividendoStock> findAllByStock(Stock stock);

    List<DividendoStock> findAllByStock(Stock stock, Sort sort);

    List<DividendoStock> findByDataBetween(LocalDate start, LocalDate end, Sort sort);

    List<DividendoStock> findByStockAndDataBetween(Stock stock, LocalDate start, LocalDate end, Sort sort);

    List<DividendoStock> findByStockAndDataBetween(Stock stock, LocalDate start, LocalDate end);
}
