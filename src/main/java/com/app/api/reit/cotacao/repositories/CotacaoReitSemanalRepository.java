package com.app.api.reit.cotacao.repositories;

import com.app.api.reit.cotacao.entities.CotacaoReitSemanal;
import com.app.api.reit.principal.entity.Reit;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CotacaoReitSemanalRepository extends JpaRepository<CotacaoReitSemanal, Long> {

    List<CotacaoReitSemanal> findByReit(Reit reit);

    List<CotacaoReitSemanal> findByReit(Reit reit, Sort sort);

    List<CotacaoReitSemanal> findByData(LocalDate dt);

    List<CotacaoReitSemanal> findByReitAndData(Reit reit, LocalDate dataCotacao);
}
