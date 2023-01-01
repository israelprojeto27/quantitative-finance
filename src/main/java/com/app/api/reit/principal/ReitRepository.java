package com.app.api.reit.principal;

import com.app.api.reit.principal.entity.Reit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReitRepository extends JpaRepository<Reit, Long> {

    Optional<Reit> findBySigla(String sigla);

    List<Reit> findBySiglaContaining(String sigla);
}
