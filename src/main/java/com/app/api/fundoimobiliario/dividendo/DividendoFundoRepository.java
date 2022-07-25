package com.app.api.fundoimobiliario.dividendo;

import com.app.api.fundoimobiliario.dividendo.entity.DividendoFundo;
import com.app.api.fundoimobiliario.principal.entity.FundoImobiliario;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DividendoFundoRepository extends JpaRepository<DividendoFundo, Long>  {

    List<DividendoFundo> findAllByFundo(FundoImobiliario fundoImobiliario);

    List<DividendoFundo> findAllByFundo(FundoImobiliario fundoImobiliario, Sort sort);

    List<DividendoFundo> findByDataBetween(LocalDate start, LocalDate end, Sort sort);

    List<DividendoFundo> findByFundoAndDataBetween(FundoImobiliario fundoImobiliario, LocalDate start, LocalDate end, Sort sort);
}
