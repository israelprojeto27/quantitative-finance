package com.app.api.fundoimobiliario.logupload;

import com.app.commons.enums.StatusUploadEnum;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;


@Builder
@Data
@Entity
@Table(name = "log_upload_fundo_imobiliario")
public class LogUploadFundoImobiliario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String filename;

    private LocalDateTime starttimeupload;

    private LocalDateTime endtimeupload;

    private StatusUploadEnum statusupload;

    private String description;

    public LogUploadFundoImobiliario() {
    }

    public LogUploadFundoImobiliario(Long id, String filename, LocalDateTime starttimeupload, LocalDateTime endtimeupload, StatusUploadEnum statusupload, String description) {
        this.id = id;
        this.filename = filename;
        this.starttimeupload = starttimeupload;
        this.endtimeupload = endtimeupload;
        this.statusupload = statusupload;
        this.description = description;
    }
}
