package com.app.api.acao.simulacao.repositories;

import com.app.api.acao.simulacao.entities.SimulaInvestimentoAcao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SimulaInvestimentoAcaoRepository extends JpaRepository<SimulaInvestimentoAcao, Long> {
}
