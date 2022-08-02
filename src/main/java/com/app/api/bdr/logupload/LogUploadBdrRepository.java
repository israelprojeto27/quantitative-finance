package com.app.api.bdr.logupload;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogUploadBdrRepository extends JpaRepository<LogUploadBdr, Long> {
}
