package com.app.api.bdr.cotacao.repositories;

import com.app.api.acao.cotacao.entities.CotacaoAcaoDiario;
import com.app.api.acao.principal.entity.Acao;
import com.app.api.bdr.cotacao.entities.CotacaoBdrDiario;
import com.app.api.bdr.principal.entity.Bdr;
import com.app.api.fundoimobiliario.cotacao.entities.CotacaoFundoDiario;
import com.app.api.fundoimobiliario.principal.entity.FundoImobiliario;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CotacaoBdrDiarioRepository extends JpaRepository<CotacaoBdrDiario, Long> {

    List<CotacaoBdrDiario> findByBdr(Bdr bdr);

    List<CotacaoBdrDiario> findByBdr(Bdr bdr, Sort sort);

    List<CotacaoBdrDiario> findByData(LocalDate dt);

    List<CotacaoBdrDiario> findByBdrAndData(Bdr bdr, LocalDate dataCotacao);


}
