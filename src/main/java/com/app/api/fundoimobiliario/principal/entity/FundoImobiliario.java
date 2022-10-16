package com.app.api.fundoimobiliario.principal.entity;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

@Builder
@Data
@Entity
@Table(name = "fundo_imobiliario")
public class FundoImobiliario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sigla;

    @Column(name = "dividend_yield")
    private Double dividendYield = 0d;

    public FundoImobiliario() {
    }

    public FundoImobiliario(Long id, String sigla, Double dividendYield) {
        this.id = id;
        this.sigla = sigla;
        this.dividendYield = dividendYield;
    }

    public FundoImobiliario(String sigla) {
        this.sigla = sigla;

    }
}
