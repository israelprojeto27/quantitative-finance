package com.app.api.bdr.increasepercent;

import com.app.api.acao.enums.PeriodoEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IncreasePercentBdrRepository extends JpaRepository<IncreasePercentBdr, Long> {

    long deleteByPeriodo(PeriodoEnum periodo);

}
