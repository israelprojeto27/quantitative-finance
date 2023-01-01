package com.app.api.reit.increasepercent;

import com.app.api.acao.enums.PeriodoEnum;
import com.app.api.reit.principal.entity.Reit;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncreasePercentReitRepository extends JpaRepository<IncreasePercentReit, Long> {

    long deleteByPeriodo(PeriodoEnum periodo);

    List<IncreasePercentReit> findByReitAndPeriodo(Reit reit, PeriodoEnum periodo);

    List<IncreasePercentReit> findByReitAndPeriodo(Reit reit, PeriodoEnum periodo, Sort sort);
}
