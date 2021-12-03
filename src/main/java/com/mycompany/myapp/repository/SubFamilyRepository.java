package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.SubFamily;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the SubFamily entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubFamilyRepository extends JpaRepository<SubFamily, Long>, JpaSpecificationExecutor<SubFamily> {}
