package com.app.api.bdr.analise;

import com.app.api.bdr.analise.entities.BdrAnalise;
import com.app.api.bdr.principal.entity.Bdr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BdrAnaliseRepository extends JpaRepository<BdrAnalise, Long> {

    Optional<BdrAnalise> findByBdr(Bdr bdr);
}
