package com.app.api.bdr.simulacao.entities;

import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;

@Builder
@Data
@Entity
@Table(name = "simula_detail_investimento_bdr")
public class SimulaDetailInvestimentoBdr {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sigla;

    @Column(name = "valor_investido")
    private Double valorInvestido;

    @Column(name = "porcentagem_valor_investido")
    private Double porcentagemValorInvestido;

    @Column(name = "ultima_cotacao_bdr")
    private Double ultimaCotacaoBdr;

    @Column(name = "data_ultima_cotacao_bdr")
    private LocalDate dataUltimaCotacaoBdr;

    @Column(name = "quantidade_cotas_bdr")
    private Double quantidadeCotasBdr;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    public SimulaDetailInvestimentoBdr() {
    }

    public SimulaDetailInvestimentoBdr(Long id, String sigla, Double valorInvestido, Double porcentagemValorInvestido, Double ultimaCotacaoBdr, LocalDate dataUltimaCotacaoBdr, Double quantidadeCotasBdr, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.sigla = sigla;
        this.valorInvestido = valorInvestido;
        this.porcentagemValorInvestido = porcentagemValorInvestido;
        this.ultimaCotacaoBdr = ultimaCotacaoBdr;
        this.dataUltimaCotacaoBdr = dataUltimaCotacaoBdr;
        this.quantidadeCotasBdr = quantidadeCotasBdr;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
