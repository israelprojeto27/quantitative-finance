package com.app.api.stock.simulacao.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "simula_detail_investimento_stock")
public class SimulaDetailInvestimentoStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sigla;

    @Column(name = "valor_investido")
    private Double valorInvestido;

    @Column(name = "porcentagem_valor_investido")
    private Double porcentagemValorInvestido;

    @Column(name = "ultima_cotacao_stock")
    private Double ultimaCotacaoStock;

    @Column(name = "data_ultima_cotacao_stock")
    private LocalDate dataUltimaCotacaoStock;

    @Column(name = "quantidade_cotas_stock")
    private Double quantidadeCotasStock;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;

}
