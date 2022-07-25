package com.app.api.parametro;

import com.app.api.parametro.dto.ParametroDTO;
import com.app.api.parametro.entity.Parametro;
import com.app.api.parametro.enums.TipoParametroEnum;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParametroRepository extends JpaRepository<Parametro, Long> {

    List<Parametro> findByTipoParametro(TipoParametroEnum tipoParametro, Sort sort);
}
