package com.app.api.reit.cotacao.repositories;

import com.app.api.reit.cotacao.entities.CotacaoReitDiario;
import com.app.api.reit.principal.entity.Reit;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CotacaoReitDiarioRepository extends JpaRepository<CotacaoReitDiario, Long> {

    List<CotacaoReitDiario> findByReit(Reit reit);

    List<CotacaoReitDiario> findByReit(Reit reit, Sort sort);

    List<CotacaoReitDiario> findByData(LocalDate dt);

    List<CotacaoReitDiario> findByReitAndData(Reit reit, LocalDate dataCotacao);
}
