package com.andrew.rental.repository;

import com.andrew.rental.domain.ActiveRent;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the ActiveRent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ActiveRentRepository extends JpaRepository<ActiveRent, Long> {

    @Query("select activeRent from ActiveRent activeRent where activeRent.client.login = ?#{principal.username}")
    List<ActiveRent> findByClientIsCurrentUser();
}
