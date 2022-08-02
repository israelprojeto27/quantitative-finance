package com.app.api.bdr.cotacao.repositories;

import com.app.api.bdr.cotacao.entities.CotacaoBdrMensal;
import com.app.api.bdr.principal.entity.Bdr;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CotacaoBdrMensalRepository extends JpaRepository<CotacaoBdrMensal, Long> {

    List<CotacaoBdrMensal> findByBdr(Bdr bdr);

    List<CotacaoBdrMensal> findByBdr(Bdr bdr, Sort sort);

    List<CotacaoBdrMensal> findByData(LocalDate dt);
}
