package com.app.api.fundoimobiliario.analise;

import com.app.api.fundoimobiliario.analise.entities.FundoImobiliarioAnalise;
import com.app.api.fundoimobiliario.principal.entity.FundoImobiliario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FundoImobiliarioAnaliseRepository extends JpaRepository<FundoImobiliarioAnalise, Long> {

    Optional<FundoImobiliarioAnalise> findByFundo(FundoImobiliario fundo);
}
