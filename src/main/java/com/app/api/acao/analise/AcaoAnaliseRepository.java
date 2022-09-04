package com.app.api.acao.analise;

import com.app.api.acao.analise.entities.AcaoAnalise;
import com.app.api.acao.principal.entity.Acao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AcaoAnaliseRepository  extends JpaRepository<AcaoAnalise, Long> {

    Optional<AcaoAnalise> findByAcao(Acao acao);
}
