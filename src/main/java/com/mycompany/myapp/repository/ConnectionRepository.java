package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Connection;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Connection entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConnectionRepository extends JpaRepository<Connection, Long> {}
