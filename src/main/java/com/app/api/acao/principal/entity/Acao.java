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


    public Acao(String sigla) {
        this.sigla = sigla;
    }


}
