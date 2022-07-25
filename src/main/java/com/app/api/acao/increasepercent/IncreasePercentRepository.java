package com.app.api.acao.increasepercent;

import com.app.api.acao.increasepercent.IncreasePercent;
import com.app.api.acao.enums.PeriodoEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IncreasePercentRepository extends JpaRepository<IncreasePercent, Long> {

    long deleteByPeriodo(PeriodoEnum periodo);

}
