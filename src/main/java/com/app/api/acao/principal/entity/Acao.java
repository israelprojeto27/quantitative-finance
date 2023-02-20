package com.app.api.acao.principal.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "acao")
public class Acao  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sigla;

    @Column(name = "dividend_yield")
    private Double dividendYield = 0d;

    @Column(name = "roe")
    private Double roe = 0d;

    @Column(name = "pvp")
    private Double pvp = 0d;

    @Column(name = "pl")
    private Double pl = 0d;

    @Column(name = "psr")
    private Double psr = 0d;

    @Column(name = "p_ativos")
    private Double pAtivos = 0d;

    @Column(name = "p_ebit")
    private Double pEbit = 0d;

    @Column(name = "marg_ebit")
    private Double margEbit = 0d;


    public Acao(String sigla) {
        this.sigla = sigla;
    }


}
