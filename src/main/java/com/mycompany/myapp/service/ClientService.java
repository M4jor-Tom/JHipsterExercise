package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Client;
import com.mycompany.myapp.repository.ClientRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Client}.
 */
@Service
@Transactional
public class ClientService {

    private final Logger log = LoggerFactory.getLogger(ClientService.class);

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    /**
     * Save a client.
     *
     * @param client the entity to save.
     * @return the persisted entity.
     */
    public Client save(Client client) {
        log.debug("Request to save Client : {}", client);
        return clientRepository.save(client);
    }

    /**
     * Partially update a client.
     *
     * @param client the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Client> partialUpdate(Client client) {
        log.debug("Request to partially update Client : {}", client);

        return clientRepository
            .findById(client.getId())
            .map(existingClient -> {
                if (client.getAddedDateTime() != null) {
                    existingClient.setAddedDateTime(client.getAddedDateTime());
                }
                if (client.getLastName() != null) {
                    existingClient.setLastName(client.getLastName());
                }
                if (client.getFirstName() != null) {
                    existingClient.setFirstName(client.getFirstName());
                }
                if (client.getEmail() != null) {
                    existingClient.setEmail(client.getEmail());
                }
                if (client.getPhone() != null) {
                    existingClient.setPhone(client.getPhone());
                }
                if (client.getAdress() != null) {
                    existingClient.setAdress(client.getAdress());
                }
                if (client.getCountry() != null) {
                    existingClient.setCountry(client.getCountry());
                }
                if (client.getPostalCode() != null) {
                    existingClient.setPostalCode(client.getPostalCode());
                }

                return existingClient;
            })
            .map(clientRepository::save);
    }

    /**
     * Get all the clients.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Client> findAll() {
        log.debug("Request to get all Clients");
        return clientRepository.findAll();
    }

    /**
     * Get one client by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Client> findOne(Long id) {
        log.debug("Request to get Client : {}", id);
        return clientRepository.findById(id);
    }

    /**
     * Delete the client by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Client : {}", id);
        clientRepository.deleteById(id);
    }
}
