package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Model;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Model entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ModelRepository extends JpaRepository<Model, Long> {}
