package com.app.api.fundoimobiliario.increasepercent;

import com.app.api.acao.enums.PeriodoEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IncreasePercentFundoRepository extends JpaRepository<IncreasePercentFundoImobiliario, Long> {

    long deleteByPeriodo(PeriodoEnum periodo);

}
