package com.app.api.fundoimobiliario.simulacao.repositories;

import com.app.api.fundoimobiliario.simulacao.entities.SimulaDetailInvestimentoFundoImobiliario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SimulaDetailInvestimentoFundoImobiliarioRepository  extends JpaRepository<SimulaDetailInvestimentoFundoImobiliario, Long> {

    Optional<SimulaDetailInvestimentoFundoImobiliario> findBySigla(String sigla);
}
