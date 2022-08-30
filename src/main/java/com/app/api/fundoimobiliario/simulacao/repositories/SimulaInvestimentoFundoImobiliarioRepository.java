package com.app.api.fundoimobiliario.simulacao.repositories;

import com.app.api.fundoimobiliario.simulacao.entities.SimulaInvestimentoFundoImobiliario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SimulaInvestimentoFundoImobiliarioRepository extends JpaRepository<SimulaInvestimentoFundoImobiliario, Long> {
}
