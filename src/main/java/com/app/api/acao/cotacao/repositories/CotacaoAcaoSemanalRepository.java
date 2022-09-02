package com.app.api.acao.cotacao.repositories;

import com.app.api.acao.cotacao.entities.CotacaoAcaoDiario;
import com.app.api.acao.principal.entity.Acao;
import com.app.api.acao.cotacao.entities.CotacaoAcaoSemanal;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CotacaoAcaoSemanalRepository extends JpaRepository<CotacaoAcaoSemanal, Long> {
    List<CotacaoAcaoSemanal> findByAcao(Acao acao);

    List<CotacaoAcaoSemanal> findByAcao(Acao acao, Sort sort);

    List<CotacaoAcaoSemanal> findByData(LocalDate dt);

    List<CotacaoAcaoSemanal> findByAcaoAndData(Acao acao, LocalDate dataCotacao);
}
