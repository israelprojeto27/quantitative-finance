package com.app.api.acao.logupload;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogUploadAcaoRepository extends JpaRepository<LogUploadAcao, Long> {
}
