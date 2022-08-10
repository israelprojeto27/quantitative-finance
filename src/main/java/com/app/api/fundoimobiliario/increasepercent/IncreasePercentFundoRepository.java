package com.app.api.fundoimobiliario.increasepercent;

import com.app.api.acao.enums.PeriodoEnum;
import com.app.api.fundoimobiliario.principal.entity.FundoImobiliario;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncreasePercentFundoRepository extends JpaRepository<IncreasePercentFundoImobiliario, Long> {

    long deleteByPeriodo(PeriodoEnum periodo);

    List<IncreasePercentFundoImobiliario> findByFundoImobiliarioAndPeriodo(FundoImobiliario fundoImobiliario, PeriodoEnum periodo, Sort dataBase);
}
