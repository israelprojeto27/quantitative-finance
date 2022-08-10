package com.app.api.bdr.principal;

import com.app.api.bdr.principal.entity.Bdr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BdrRepository extends JpaRepository<Bdr, Long> {

    Optional<Bdr> findBySigla(String sigla);

    List<Bdr> findBySiglaContaining(String sigla);
}
