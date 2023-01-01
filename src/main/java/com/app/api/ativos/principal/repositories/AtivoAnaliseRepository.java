package com.app.api.ativos.principal.repositories;

import com.app.api.acao.principal.entity.Acao;
import com.app.api.ativos.principal.entities.AtivoAnalise;
import com.app.api.bdr.principal.entity.Bdr;
import com.app.api.fundoimobiliario.principal.entity.FundoImobiliario;
import com.app.api.stock.principal.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AtivoAnaliseRepository  extends JpaRepository<AtivoAnalise, Long> {

    Optional<AtivoAnalise> findByAcao(Acao acao);

    Optional<AtivoAnalise> findByBdr(Bdr bdr);

    Optional<AtivoAnalise> findByFundo(FundoImobiliario fundoImobiliario);

    Optional<AtivoAnalise> findByStock(Stock stock);
}

