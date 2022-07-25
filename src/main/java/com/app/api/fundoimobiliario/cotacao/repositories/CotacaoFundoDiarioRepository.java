package com.app.api.fundoimobiliario.cotacao.repositories;

import com.app.api.fundoimobiliario.cotacao.entities.CotacaoFundoDiario;
import com.app.api.fundoimobiliario.principal.entity.FundoImobiliario;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CotacaoFundoDiarioRepository extends JpaRepository<CotacaoFundoDiario, Long> {

    List<CotacaoFundoDiario> findByFundo(FundoImobiliario fundoImobiliario);

    List<CotacaoFundoDiario> findByFundo(FundoImobiliario fundoImobiliario, Sort sort);

    List<CotacaoFundoDiario> findByData(LocalDate dt);
}
