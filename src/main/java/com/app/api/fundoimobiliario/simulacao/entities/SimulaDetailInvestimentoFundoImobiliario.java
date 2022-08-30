package com.app.api.fundoimobiliario.simulacao.entities;

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
@Table(name = "simula_detail_investimento_fundo_imobiliario")
public class SimulaDetailInvestimentoFundoImobiliario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sigla;

    @Column(name = "valor_investido")
    private Double valorInvestido;

    @Column(name = "porcentagem_valor_investido")
    private Double porcentagemValorInvestido;

    @Column(name = "ultima_cotacao_fundo")
    private Double ultimaCotacaoFundo;

    @Column(name = "data_ultima_cotacao_fundo")
    private LocalDate dataUltimaCotacaoFundo;

    @Column(name = "quantidade_cotas_fundo")
    private Double quantidadeCotasFundo;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    public SimulaDetailInvestimentoFundoImobiliario() {
    }

    public SimulaDetailInvestimentoFundoImobiliario(Long id, String sigla, Double valorInvestido, Double porcentagemValorInvestido, Double ultimaCotacaoFundo, LocalDate dataUltimaCotacaoFundo, Double quantidadeCotasFundo, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.sigla = sigla;
        this.valorInvestido = valorInvestido;
        this.porcentagemValorInvestido = porcentagemValorInvestido;
        this.ultimaCotacaoFundo = ultimaCotacaoFundo;
        this.dataUltimaCotacaoFundo = dataUltimaCotacaoFundo;
        this.quantidadeCotasFundo = quantidadeCotasFundo;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
