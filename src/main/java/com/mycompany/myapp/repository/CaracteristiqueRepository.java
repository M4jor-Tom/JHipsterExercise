package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Caracteristique;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Caracteristique entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CaracteristiqueRepository extends JpaRepository<Caracteristique, Long> {}
