package com.app.api.acao.increasepercent;

import com.app.api.acao.enums.PeriodoEnum;
import com.app.api.acao.principal.entity.Acao;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncreasePercentAcaoRepository extends JpaRepository<IncreasePercentAcao, Long> {

    long deleteByPeriodo(PeriodoEnum periodo);

    List<IncreasePercentAcao> findByAcaoAndPeriodo(Acao acao, PeriodoEnum periodo);

    List<IncreasePercentAcao> findByAcaoAndPeriodo(Acao acao, PeriodoEnum periodo, Sort sort);
}
