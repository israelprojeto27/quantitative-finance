package com.app.api.fundoimobiliario.principal;

import com.app.api.fundoimobiliario.principal.entity.FundoImobiliario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FundoImobiliarioRepository extends JpaRepository<FundoImobiliario, Long> {

    Optional<FundoImobiliario> findBySigla(String sigla);
}
