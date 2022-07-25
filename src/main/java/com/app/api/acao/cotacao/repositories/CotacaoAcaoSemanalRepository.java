package com.app.api.acao.cotacao.repositories;

import com.app.api.acao.principal.entity.Acao;
import com.app.api.acao.cotacao.entities.CotacaoAcaoSemanal;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CotacaoAcaoSemanalRepository extends JpaRepository<CotacaoAcaoSemanal, Long> {
    List<CotacaoAcaoSemanal> findByAcao(Acao acao);

    List<CotacaoAcaoSemanal> findByAcao(Acao acao, Sort sort);
}
