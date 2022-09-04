package com.app.commons.basic.analise;

import com.app.commons.basic.analise.dto.AtivoAnaliseDTO;

import java.util.List;

public interface BaseAtivoAnaliseService {

    boolean addAtivoAnalise(String sigla);

    List<AtivoAnaliseDTO> findAll();

    boolean deleteAtivoAnalise(String sigla);
}
