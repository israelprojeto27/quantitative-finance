package com.app.api.bdr.cotacao.repositories;

import com.app.api.bdr.cotacao.entities.CotacaoBdrSemanal;
import com.app.api.bdr.principal.entity.Bdr;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CotacaoBdrSemanalRepository extends JpaRepository<CotacaoBdrSemanal, Long> {

    List<CotacaoBdrSemanal> findByBdr(Bdr bdr);

    List<CotacaoBdrSemanal> findByBdr(Bdr bdr, Sort sort);

    List<CotacaoBdrSemanal> findByData(LocalDate dt);
}
