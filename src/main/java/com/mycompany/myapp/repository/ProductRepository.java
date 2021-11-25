package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Product;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Product entity.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query(
        value = "select distinct product from Product product left join fetch product.tags",
        countQuery = "select count(distinct product) from Product product"
    )
    Page<Product> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct product from Product product left join fetch product.tags")
    List<Product> findAllWithEagerRelationships();

    @Query("select product from Product product left join fetch product.tags where product.id =:id")
    Optional<Product> findOneWithEagerRelationships(@Param("id") Long id);
}
