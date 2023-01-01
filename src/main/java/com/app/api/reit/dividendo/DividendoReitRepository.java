package com.app.api.reit.dividendo;

import com.app.api.reit.dividendo.entity.DividendoReit;
import com.app.api.reit.principal.entity.Reit;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DividendoReitRepository extends JpaRepository<DividendoReit, Long> {

    List<DividendoReit> findAllByReit(Reit reit);

    List<DividendoReit> findAllByReit(Reit reit, Sort sort);

    List<DividendoReit> findByDataBetween(LocalDate start, LocalDate end, Sort sort);

    List<DividendoReit> findByReitAndDataBetween(Reit reit, LocalDate start, LocalDate end, Sort sort);

    List<DividendoReit> findByReitAndDataBetween(Reit reit, LocalDate start, LocalDate end);
}
