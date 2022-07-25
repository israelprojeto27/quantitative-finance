package com.app.api.parametro.entity;

import com.app.api.parametro.dto.ParametroDTO;
import com.app.api.parametro.enums.TipoParametroEnum;
import com.app.api.parametro.enums.TipoValorParametroEnum;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;


@Builder
@Data
@Entity
@Table(name = "parametro")
public class Parametro {
    public Parametro() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tipo_parametro")
    private TipoParametroEnum tipoParametro;

    @Column(name = "tipo_valor_parametro")
    private TipoValorParametroEnum tipoValorParametro;

    private String valor;

    private String obs;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    public Parametro(Long id, TipoParametroEnum tipoParametro, TipoValorParametroEnum tipoValorParametro, String valor, String obs, Timestamp createdAt, Timestamp updatedAt) {
        this.id = id;
        this.tipoParametro = tipoParametro;
        this.tipoValorParametro = tipoValorParametro;
        this.valor = valor;
        this.obs = obs;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static ParametroDTO fromEntity(Parametro entity) {
        return ParametroDTO.builder()
                .id(entity.getId())
                .tipoParametro(entity.getTipoParametro())
                .tipoValorParametro(entity.getTipoValorParametro())
                .valor(entity.getValor())
                .obs(entity.getObs())
                .build();
    }
}
