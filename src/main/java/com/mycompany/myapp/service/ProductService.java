package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Product;
import com.mycompany.myapp.repository.ProductRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Product}.
 */
@Service
@Transactional
public class ProductService {

    private final Logger log = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Save a product.
     *
     * @param product the entity to save.
     * @return the persisted entity.
     */
    public Product save(Product product) {
        log.debug("Request to save Product : {}", product);
        return productRepository.save(product);
    }

    /**
     * Partially update a product.
     *
     * @param product the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Product> partialUpdate(Product product) {
        log.debug("Request to partially update Product : {}", product);

        return productRepository
            .findById(product.getId())
            .map(existingProduct -> {
                if (product.getDescription() != null) {
                    existingProduct.setDescription(product.getDescription());
                }
                if (product.getPhotoId() != null) {
                    existingProduct.setPhotoId(product.getPhotoId());
                }
                if (product.getStock() != null) {
                    existingProduct.setStock(product.getStock());
                }
                if (product.getPrice() != null) {
                    existingProduct.setPrice(product.getPrice());
                }
                if (product.getModelName() != null) {
                    existingProduct.setModelName(product.getModelName());
                }
                if (product.getColor() != null) {
                    existingProduct.setColor(product.getColor());
                }

                return existingProduct;
            })
            .map(productRepository::save);
    }

    /**
     * Get all the products.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Product> findAll() {
        log.debug("Request to get all Products");
        return productRepository.findAllWithEagerRelationships();
    }

    /**
     * Get all the products with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Product> findAllWithEagerRelationships(Pageable pageable) {
        return productRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one product by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Product> findOne(Long id) {
        log.debug("Request to get Product : {}", id);
        return productRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the product by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Product : {}", id);
        productRepository.deleteById(id);
    }
}
