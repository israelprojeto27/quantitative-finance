package com.app.api.reit.analise;

import com.app.api.reit.analise.entities.ReitAnalise;
import com.app.api.reit.principal.entity.Reit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReitAnaliseRepository extends JpaRepository<ReitAnalise, Long> {

    Optional<ReitAnalise> findByReit(Reit reit);
}
