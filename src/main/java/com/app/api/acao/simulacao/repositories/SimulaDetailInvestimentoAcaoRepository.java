package com.app.api.acao.simulacao.repositories;

import com.app.api.acao.simulacao.entities.SimulaDetailInvestimentoAcao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SimulaDetailInvestimentoAcaoRepository extends JpaRepository<SimulaDetailInvestimentoAcao, Long> {

    Optional<SimulaDetailInvestimentoAcao> findBySigla(String sigla);
}
