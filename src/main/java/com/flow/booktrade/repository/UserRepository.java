package com.flow.booktrade.repository;

import com.flow.booktrade.domain.RUser;

import java.time.ZonedDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the User entity.
 */
public interface UserRepository extends JpaRepository<RUser, Long> {

    Optional<RUser> findOneByActivationKey(String activationKey);

    List<RUser> findAllByActivatedIsFalseAndCreatedDateBefore(ZonedDateTime dateTime);

    Optional<RUser> findOneByResetKey(String resetKey);

    Optional<RUser> findOneByEmail(String email);

    Optional<RUser> findOneByLogin(String login);
}
