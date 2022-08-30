package com.app.api.fundoimobiliario.simulacao.entities;

import com.app.api.bdr.simulacao.entities.SimulaInvestimentoBdr;
import com.app.commons.basic.simulacao.dto.SaveSimulacaoInvestimentoAtivoDTO;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Builder
@Data
@Entity
@Table(name = "simula_investimento_fundo_imobiliario")
public class SimulaInvestimentoFundoImobiliario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "valor_investimento")
    private Double valorInvestimento;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    public SimulaInvestimentoFundoImobiliario() {
    }

    public SimulaInvestimentoFundoImobiliario(Long id, Double valorInvestimento, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.valorInvestimento = valorInvestimento;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static SimulaInvestimentoFundoImobiliario toEntity(SaveSimulacaoInvestimentoAtivoDTO dto) {
        return SimulaInvestimentoFundoImobiliario.builder()
                .valorInvestimento(dto.getValorInvestimento())
                .build();
    }
}
