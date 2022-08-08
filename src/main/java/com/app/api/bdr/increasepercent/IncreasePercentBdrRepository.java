package com.app.api.bdr.increasepercent;

import com.app.api.acao.enums.PeriodoEnum;
import com.app.api.bdr.principal.entity.Bdr;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncreasePercentBdrRepository extends JpaRepository<IncreasePercentBdr, Long> {

    long deleteByPeriodo(PeriodoEnum periodo);

    List<IncreasePercentBdr> findByBdrAndPeriodo(Bdr bdr, PeriodoEnum periodo, Sort sort);
}
