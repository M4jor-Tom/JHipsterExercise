package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Connexion;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Connexion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConnexionRepository extends JpaRepository<Connexion, Long> {}
