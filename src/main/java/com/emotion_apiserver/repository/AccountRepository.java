package com.emotion_apiserver.repository;

import com.emotion_apiserver.domain.Account;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    @EntityGraph(attributePaths = {"accountRoleList"})
    @Query("select a from Account a where a.email = :email")
    Account getWithRoles(@Param("email") String email);

    boolean existsByNickname(String nickname);

    Optional<Account> findByNickname(String nickname);
}
