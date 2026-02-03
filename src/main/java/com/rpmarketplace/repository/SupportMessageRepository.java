package com.rpmarketplace.repository;

import com.rpmarketplace.model.SupportMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupportMessageRepository extends JpaRepository<SupportMessage, Long> {
}
