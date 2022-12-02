package com.app.api.stock.increasepercent;

import com.app.api.acao.enums.PeriodoEnum;
import com.app.api.acao.principal.entity.Acao;
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
@Table(name = "increase_percent_stock")
public class IncreasePercentStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_base")
    private LocalDate dataBase;

    @Column(name = "data_reference")
    private LocalDate dataReference;

    @Column(name = "periodo")
    private PeriodoEnum periodo;

    @Column(name = "intervalo_periodo")
    private Integer intervaloPeriodo; // Se periodo = 'diario' e intervalo_periodo = 5 entao significa que percentual calculado eh referente aos 5 ultimos dias

    private Double percentual;

    @Column(name = "valor_fechamento_atual")
    private Double valorFechamentoAtual;


    @Column(name = "valor_fechamento_anterior")
    private Double valorFechamentoAnterior;


    @ManyToOne
    @JoinColumn(name = "stock_id")
    private Stock stock;


    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;

}
