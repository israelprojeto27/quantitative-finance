package com.app.api.fundoimobiliario.logupload;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogUploadFundoImobiliarioRepository extends JpaRepository<LogUploadFundoImobiliario, Long> {
}
