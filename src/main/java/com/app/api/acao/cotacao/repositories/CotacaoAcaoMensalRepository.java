package com.app.api.acao.cotacao.repositories;

import com.app.api.acao.cotacao.entities.CotacaoAcaoMensal;
import com.app.api.acao.cotacao.entities.CotacaoAcaoSemanal;
import com.app.api.acao.principal.entity.Acao;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CotacaoAcaoMensalRepository extends JpaRepository<CotacaoAcaoMensal, Long> {
    List<CotacaoAcaoMensal> findByAcao(Acao acao);

    List<CotacaoAcaoMensal> findByAcao(Acao acao, Sort sort);

    List<CotacaoAcaoMensal> findByData(LocalDate dt);

    List<CotacaoAcaoMensal> findByAcaoAndData(Acao acao, LocalDate dataCotacao);
}
