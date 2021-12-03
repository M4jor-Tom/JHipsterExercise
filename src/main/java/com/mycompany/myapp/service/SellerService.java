package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Seller;
import com.mycompany.myapp.repository.SellerRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Seller}.
 */
@Service
@Transactional
public class SellerService {

    private final Logger log = LoggerFactory.getLogger(SellerService.class);

    private final SellerRepository sellerRepository;

    public SellerService(SellerRepository sellerRepository) {
        this.sellerRepository = sellerRepository;
    }

    /**
     * Save a seller.
     *
     * @param seller the entity to save.
     * @return the persisted entity.
     */
    public Seller save(Seller seller) {
        log.debug("Request to save Seller : {}", seller);
        return sellerRepository.save(seller);
    }

    /**
     * Partially update a seller.
     *
     * @param seller the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Seller> partialUpdate(Seller seller) {
        log.debug("Request to partially update Seller : {}", seller);

        return sellerRepository
            .findById(seller.getId())
            .map(existingSeller -> {
                if (seller.getSocialReason() != null) {
                    existingSeller.setSocialReason(seller.getSocialReason());
                }
                if (seller.getAddress() != null) {
                    existingSeller.setAddress(seller.getAddress());
                }
                if (seller.getSiretNumber() != null) {
                    existingSeller.setSiretNumber(seller.getSiretNumber());
                }
                if (seller.getPhone() != null) {
                    existingSeller.setPhone(seller.getPhone());
                }
                if (seller.getEmail() != null) {
                    existingSeller.setEmail(seller.getEmail());
                }

                return existingSeller;
            })
            .map(sellerRepository::save);
    }

    /**
     * Get all the sellers.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Seller> findAll() {
        log.debug("Request to get all Sellers");
        return sellerRepository.findAll();
    }

    /**
     * Get one seller by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Seller> findOne(Long id) {
        log.debug("Request to get Seller : {}", id);
        return sellerRepository.findById(id);
    }

    /**
     * Delete the seller by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Seller : {}", id);
        sellerRepository.deleteById(id);
    }
}
