package com.app.api.fundoimobiliario.cotacao.repositories;

import com.app.api.fundoimobiliario.cotacao.entities.CotacaoFundoMensal;
import com.app.api.fundoimobiliario.principal.entity.FundoImobiliario;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CotacaoFundoMensalRepository extends JpaRepository<CotacaoFundoMensal, Long> {

    List<CotacaoFundoMensal> findByFundo(FundoImobiliario fundoImobiliario);

    List<CotacaoFundoMensal> findByFundo(FundoImobiliario fundoImobiliario, Sort sort);

    List<CotacaoFundoMensal> findByData(LocalDate dt);
}
