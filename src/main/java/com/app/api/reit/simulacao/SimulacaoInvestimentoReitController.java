package com.app.api.reit.simulacao;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/simula-investimento-reit")
@Tag(name = "Reit - Simulação Investimento")
public class SimulacaoInvestimentoReitController {

    @Autowired
    SimulaInvestimentoReitService service;
}
