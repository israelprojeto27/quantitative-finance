package com.app.api.acao.simulacao.entities;

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
@Table(name = "simula_detail_investimento_acao")
public class SimulaDetailInvestimentoAcao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sigla;

    @Column(name = "valor_investido")
    private Double valorInvestido;

    @Column(name = "porcentagem_valor_investido")
    private Double porcentagemValorInvestido;

    @Column(name = "ultima_cotacao_acao")
    private Double ultimaCotacaoAcao;

    @Column(name = "data_ultima_cotacao_acao")
    private LocalDate dataUltimaCotacaoAcao;

    @Column(name = "quantidade_cotas_acao")
    private Double quantidadeCotasAcao;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    public SimulaDetailInvestimentoAcao() {
    }

    public SimulaDetailInvestimentoAcao(Long id, String sigla, Double valorInvestido, Double porcentagemValorInvestido, Double ultimaCotacaoAcao, LocalDate dataUltimaCotacaoAcao, Double quantidadeCotasAcao, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.sigla = sigla;
        this.valorInvestido = valorInvestido;
        this.porcentagemValorInvestido = porcentagemValorInvestido;
        this.ultimaCotacaoAcao = ultimaCotacaoAcao;
        this.dataUltimaCotacaoAcao = dataUltimaCotacaoAcao;
        this.quantidadeCotasAcao = quantidadeCotasAcao;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
