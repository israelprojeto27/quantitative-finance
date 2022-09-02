package com.app.api.fundoimobiliario.cotacao.repositories;

import com.app.api.fundoimobiliario.cotacao.entities.CotacaoFundoDiario;
import com.app.api.fundoimobiliario.cotacao.entities.CotacaoFundoSemanal;
import com.app.api.fundoimobiliario.principal.entity.FundoImobiliario;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CotacaoFundoSemanalRepository extends JpaRepository<CotacaoFundoSemanal, Long> {

    List<CotacaoFundoSemanal> findByFundo(FundoImobiliario fundoImobiliario);

    List<CotacaoFundoSemanal> findByFundo(FundoImobiliario fundoImobiliario, Sort sort);

    List<CotacaoFundoSemanal> findByData(LocalDate dt);
    List<CotacaoFundoSemanal> findByFundoAndData(FundoImobiliario fundoImobiliario, LocalDate dataCotacao);
}
