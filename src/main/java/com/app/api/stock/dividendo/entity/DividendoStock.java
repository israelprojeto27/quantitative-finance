package com.app.api.stock.dividendo.entity;

import com.app.api.acao.cotacao.entities.CotacaoAcaoDiario;
import com.app.api.acao.principal.entity.Acao;
import com.app.api.stock.cotacao.entities.CotacaoStockDiario;
import com.app.api.stock.principal.entity.Stock;
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
@Table(name = "dividendo_stock")
public class DividendoStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate data;

    private Double dividend;

    @ManyToOne
    @JoinColumn(name = "stock_id")
    private Stock stock;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;


    public static DividendoStock toEntity(CotacaoStockDiario entity) {
        return DividendoStock.builder()
                .data(entity.getData())
                .dividend(entity.getDividend())
                .stock(entity.getStock())
                .build();
    }
}
