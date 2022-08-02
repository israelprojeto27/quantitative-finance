package com.app.api.bdr;

import com.app.api.bdr.principal.entity.Bdr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BdrRepository extends JpaRepository<Bdr, Long> {

    Optional<Bdr> findBySigla(String sigla);
}
