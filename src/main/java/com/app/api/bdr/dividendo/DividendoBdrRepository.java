package com.app.api.bdr.dividendo;

import com.app.api.bdr.dividendo.entity.DividendoBdr;
import com.app.api.bdr.principal.entity.Bdr;
import com.app.api.fundoimobiliario.dividendo.entity.DividendoFundo;
import com.app.api.fundoimobiliario.principal.entity.FundoImobiliario;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DividendoBdrRepository extends JpaRepository<DividendoBdr, Long>  {

    List<DividendoBdr> findAllByBdr(Bdr bdr);

    List<DividendoBdr> findAllByBdr(Bdr bdr, Sort sort);

    List<DividendoBdr> findByDataBetween(LocalDate start, LocalDate end, Sort sort);

    List<DividendoBdr> findByBdrAndDataBetween(Bdr bdr, LocalDate start, LocalDate end, Sort sort);


}
