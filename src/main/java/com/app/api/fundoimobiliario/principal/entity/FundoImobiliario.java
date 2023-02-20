package com.app.api.fundoimobiliario.principal.entity;

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
@Table(name = "fundo_imobiliario")
public class FundoImobiliario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sigla;

    @Column(name = "dividend_yield")
    private Double dividendYield = 0d;

    @Column(name = "dividendo_cota")
    private Double dividendoCota = 0d;

    @Column(name = "ffo_yield")
    private Double ffoYield = 0d;

    @Column(name = "ffo_cota")
    private Double ffoCota = 0d;

    @Column(name = "pvp")
    private Double pvp = 0d;

    @Column(name = "vp_cota")
    private Double vpCota = 0d;

    @Column(name = "valor_mercado")
    private Double valorMercado = 0d;

    @Column(name = "nro_cota")
    private Double nroCota = 0d;

    @Column(name = "qtd_imoveis")
    private Double qtdImoveis = 0d;

    @Column(name = "cap_rate")
    private Double capRate = 0d;

    @Column(name = "qtd_unid")
    private Double qtdUnid = 0d;

    @Column(name = "aluguel_m2")
    private Double aluguelM2 = 0d;

    @Column(name = "vacancia_media")
    private Double vacanciaMedia = 0d;

    @Column(name = "imoveis_pl")
    private Double imoveisPl = 0d;

    @Column(name = "preco_m2")
    private Double precoM2 = 0d;



    public FundoImobiliario(String sigla) {
        this.sigla = sigla;

    }
}
