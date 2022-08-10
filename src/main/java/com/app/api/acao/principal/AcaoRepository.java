package com.app.api.acao.principal;

import com.app.api.acao.principal.entity.Acao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AcaoRepository extends JpaRepository<Acao, Long> {

    Optional<Acao> findBySigla(String sigla);

    List<Acao> findBySiglaContaining(String sigla);
}
