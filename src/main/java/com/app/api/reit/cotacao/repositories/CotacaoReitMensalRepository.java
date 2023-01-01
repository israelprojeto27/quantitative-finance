package com.app.api.reit.cotacao.repositories;

import com.app.api.reit.cotacao.entities.CotacaoReitMensal;
import com.app.api.reit.principal.entity.Reit;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface CotacaoReitMensalRepository extends JpaRepository<CotacaoReitMensal, Long> {

    List<CotacaoReitMensal> findByReit(Reit reit);

    List<CotacaoReitMensal> findByReit(Reit reit, Sort sort);

    List<CotacaoReitMensal> findByData(LocalDate dt);

    List<CotacaoReitMensal> findByReitAndData(Reit reit, LocalDate dataCotacao);
}
