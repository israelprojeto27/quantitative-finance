package com.app.api.acao.dividendo;

import com.app.api.acao.dividendo.entity.DividendoAcao;
import com.app.api.acao.principal.entity.Acao;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public interface DividendoAcaoRepository extends JpaRepository<DividendoAcao, Long>  {

    List<DividendoAcao> findAllByAcao(Acao acao);

    List<DividendoAcao> findAllByAcao(Acao acao, Sort sort);

    List<DividendoAcao> findByDataBetween(LocalDate start, LocalDate end, Sort sort);

    List<DividendoAcao> findByAcaoAndDataBetween(Acao acao, LocalDate start, LocalDate end, Sort sort);

    List<DividendoAcao> findByAcaoAndDataBetween(Acao acao, LocalDate start, LocalDate end);
}
