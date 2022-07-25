package com.app.api.acao.cotacao.repositories;

import com.app.api.acao.principal.entity.Acao;
import com.app.api.acao.cotacao.entities.CotacaoAcaoMensal;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CotacaoAcaoMensalRepository extends JpaRepository<CotacaoAcaoMensal, Long> {
    List<CotacaoAcaoMensal> findByAcao(Acao acao);

    List<CotacaoAcaoMensal> findByAcao(Acao acao, Sort sort);
}
