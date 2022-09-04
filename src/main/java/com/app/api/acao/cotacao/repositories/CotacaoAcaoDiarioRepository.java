package com.app.api.acao.cotacao.repositories;

import com.app.api.acao.cotacao.entities.CotacaoAcaoDiario;
import com.app.api.acao.principal.entity.Acao;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CotacaoAcaoDiarioRepository extends JpaRepository<CotacaoAcaoDiario, Long> {

    List<CotacaoAcaoDiario> findByAcao(Acao acao);

    List<CotacaoAcaoDiario> findByAcao(Acao acao, Sort sort);

    List<CotacaoAcaoDiario> findByData(LocalDate dt);

    List<CotacaoAcaoDiario> findByAcaoAndData(Acao acao, LocalDate dataCotacao);
}
