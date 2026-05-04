package com.javaeasybank.risk.repository;

import com.javaeasybank.risk.core.enums.BlacklistType;
import com.javaeasybank.risk.entity.Blacklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BlackListRepository extends JpaRepository<Blacklist, Long> {

    @Query("SELECT b FROM Blacklist b WHERE b.listType = :type AND b.listValue = :value AND b.status = true")
    Optional<Blacklist> findActiveBlacklist(BlacklistType type, String value);

    @Query("SELECT b FROM Blacklist b WHERE b.listType = :type AND b.listValue = :value")
    Optional<Blacklist> findByBusinessKey(BlacklistType type, String value);
}
