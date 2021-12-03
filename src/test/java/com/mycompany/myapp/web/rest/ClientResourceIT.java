package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Client;
import com.mycompany.myapp.domain.Connection;
import com.mycompany.myapp.domain.Order;
import com.mycompany.myapp.repository.ClientRepository;
import com.mycompany.myapp.service.criteria.ClientCriteria;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ClientResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ClientResourceIT {

    private static final ZonedDateTime DEFAULT_ADDED_DATE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_ADDED_DATE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_ADDED_DATE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_ADRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADRESS = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final String DEFAULT_POSTAL_CODE = "AAAAAAAAAA";
    private static final String UPDATED_POSTAL_CODE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/clients";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restClientMockMvc;

    private Client client;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Client createEntity(EntityManager em) {
        Client client = new Client()
            .addedDateTime(DEFAULT_ADDED_DATE_TIME)
            .lastName(DEFAULT_LAST_NAME)
            .firstName(DEFAULT_FIRST_NAME)
            .email(DEFAULT_EMAIL)
            .phone(DEFAULT_PHONE)
            .adress(DEFAULT_ADRESS)
            .country(DEFAULT_COUNTRY)
            .postalCode(DEFAULT_POSTAL_CODE);
        return client;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Client createUpdatedEntity(EntityManager em) {
        Client client = new Client()
            .addedDateTime(UPDATED_ADDED_DATE_TIME)
            .lastName(UPDATED_LAST_NAME)
            .firstName(UPDATED_FIRST_NAME)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .adress(UPDATED_ADRESS)
            .country(UPDATED_COUNTRY)
            .postalCode(UPDATED_POSTAL_CODE);
        return client;
    }

    @BeforeEach
    public void initTest() {
        client = createEntity(em);
    }

    @Test
    @Transactional
    void createClient() throws Exception {
        int databaseSizeBeforeCreate = clientRepository.findAll().size();
        // Create the Client
        restClientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(client)))
            .andExpect(status().isCreated());

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeCreate + 1);
        Client testClient = clientList.get(clientList.size() - 1);
        assertThat(testClient.getAddedDateTime()).isEqualTo(DEFAULT_ADDED_DATE_TIME);
        assertThat(testClient.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testClient.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testClient.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testClient.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testClient.getAdress()).isEqualTo(DEFAULT_ADRESS);
        assertThat(testClient.getCountry()).isEqualTo(DEFAULT_COUNTRY);
        assertThat(testClient.getPostalCode()).isEqualTo(DEFAULT_POSTAL_CODE);
    }

    @Test
    @Transactional
    void createClientWithExistingId() throws Exception {
        // Create the Client with an existing ID
        client.setId(1L);

        int databaseSizeBeforeCreate = clientRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restClientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(client)))
            .andExpect(status().isBadRequest());

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAddedDateTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = clientRepository.findAll().size();
        // set the field null
        client.setAddedDateTime(null);

        // Create the Client, which fails.

        restClientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(client)))
            .andExpect(status().isBadRequest());

        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = clientRepository.findAll().size();
        // set the field null
        client.setLastName(null);

        // Create the Client, which fails.

        restClientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(client)))
            .andExpect(status().isBadRequest());

        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = clientRepository.findAll().size();
        // set the field null
        client.setFirstName(null);

        // Create the Client, which fails.

        restClientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(client)))
            .andExpect(status().isBadRequest());

        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCountryIsRequired() throws Exception {
        int databaseSizeBeforeTest = clientRepository.findAll().size();
        // set the field null
        client.setCountry(null);

        // Create the Client, which fails.

        restClientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(client)))
            .andExpect(status().isBadRequest());

        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPostalCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = clientRepository.findAll().size();
        // set the field null
        client.setPostalCode(null);

        // Create the Client, which fails.

        restClientMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(client)))
            .andExpect(status().isBadRequest());

        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllClients() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList
        restClientMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(client.getId().intValue())))
            .andExpect(jsonPath("$.[*].addedDateTime").value(hasItem(sameInstant(DEFAULT_ADDED_DATE_TIME))))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].adress").value(hasItem(DEFAULT_ADRESS)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].postalCode").value(hasItem(DEFAULT_POSTAL_CODE)));
    }

    @Test
    @Transactional
    void getClient() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get the client
        restClientMockMvc
            .perform(get(ENTITY_API_URL_ID, client.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(client.getId().intValue()))
            .andExpect(jsonPath("$.addedDateTime").value(sameInstant(DEFAULT_ADDED_DATE_TIME)))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.adress").value(DEFAULT_ADRESS))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY))
            .andExpect(jsonPath("$.postalCode").value(DEFAULT_POSTAL_CODE));
    }

    @Test
    @Transactional
    void getClientsByIdFiltering() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        Long id = client.getId();

        defaultClientShouldBeFound("id.equals=" + id);
        defaultClientShouldNotBeFound("id.notEquals=" + id);

        defaultClientShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultClientShouldNotBeFound("id.greaterThan=" + id);

        defaultClientShouldBeFound("id.lessThanOrEqual=" + id);
        defaultClientShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllClientsByAddedDateTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where addedDateTime equals to DEFAULT_ADDED_DATE_TIME
        defaultClientShouldBeFound("addedDateTime.equals=" + DEFAULT_ADDED_DATE_TIME);

        // Get all the clientList where addedDateTime equals to UPDATED_ADDED_DATE_TIME
        defaultClientShouldNotBeFound("addedDateTime.equals=" + UPDATED_ADDED_DATE_TIME);
    }

    @Test
    @Transactional
    void getAllClientsByAddedDateTimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where addedDateTime not equals to DEFAULT_ADDED_DATE_TIME
        defaultClientShouldNotBeFound("addedDateTime.notEquals=" + DEFAULT_ADDED_DATE_TIME);

        // Get all the clientList where addedDateTime not equals to UPDATED_ADDED_DATE_TIME
        defaultClientShouldBeFound("addedDateTime.notEquals=" + UPDATED_ADDED_DATE_TIME);
    }

    @Test
    @Transactional
    void getAllClientsByAddedDateTimeIsInShouldWork() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where addedDateTime in DEFAULT_ADDED_DATE_TIME or UPDATED_ADDED_DATE_TIME
        defaultClientShouldBeFound("addedDateTime.in=" + DEFAULT_ADDED_DATE_TIME + "," + UPDATED_ADDED_DATE_TIME);

        // Get all the clientList where addedDateTime equals to UPDATED_ADDED_DATE_TIME
        defaultClientShouldNotBeFound("addedDateTime.in=" + UPDATED_ADDED_DATE_TIME);
    }

    @Test
    @Transactional
    void getAllClientsByAddedDateTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where addedDateTime is not null
        defaultClientShouldBeFound("addedDateTime.specified=true");

        // Get all the clientList where addedDateTime is null
        defaultClientShouldNotBeFound("addedDateTime.specified=false");
    }

    @Test
    @Transactional
    void getAllClientsByAddedDateTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where addedDateTime is greater than or equal to DEFAULT_ADDED_DATE_TIME
        defaultClientShouldBeFound("addedDateTime.greaterThanOrEqual=" + DEFAULT_ADDED_DATE_TIME);

        // Get all the clientList where addedDateTime is greater than or equal to UPDATED_ADDED_DATE_TIME
        defaultClientShouldNotBeFound("addedDateTime.greaterThanOrEqual=" + UPDATED_ADDED_DATE_TIME);
    }

    @Test
    @Transactional
    void getAllClientsByAddedDateTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where addedDateTime is less than or equal to DEFAULT_ADDED_DATE_TIME
        defaultClientShouldBeFound("addedDateTime.lessThanOrEqual=" + DEFAULT_ADDED_DATE_TIME);

        // Get all the clientList where addedDateTime is less than or equal to SMALLER_ADDED_DATE_TIME
        defaultClientShouldNotBeFound("addedDateTime.lessThanOrEqual=" + SMALLER_ADDED_DATE_TIME);
    }

    @Test
    @Transactional
    void getAllClientsByAddedDateTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where addedDateTime is less than DEFAULT_ADDED_DATE_TIME
        defaultClientShouldNotBeFound("addedDateTime.lessThan=" + DEFAULT_ADDED_DATE_TIME);

        // Get all the clientList where addedDateTime is less than UPDATED_ADDED_DATE_TIME
        defaultClientShouldBeFound("addedDateTime.lessThan=" + UPDATED_ADDED_DATE_TIME);
    }

    @Test
    @Transactional
    void getAllClientsByAddedDateTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where addedDateTime is greater than DEFAULT_ADDED_DATE_TIME
        defaultClientShouldNotBeFound("addedDateTime.greaterThan=" + DEFAULT_ADDED_DATE_TIME);

        // Get all the clientList where addedDateTime is greater than SMALLER_ADDED_DATE_TIME
        defaultClientShouldBeFound("addedDateTime.greaterThan=" + SMALLER_ADDED_DATE_TIME);
    }

    @Test
    @Transactional
    void getAllClientsByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where lastName equals to DEFAULT_LAST_NAME
        defaultClientShouldBeFound("lastName.equals=" + DEFAULT_LAST_NAME);

        // Get all the clientList where lastName equals to UPDATED_LAST_NAME
        defaultClientShouldNotBeFound("lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllClientsByLastNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where lastName not equals to DEFAULT_LAST_NAME
        defaultClientShouldNotBeFound("lastName.notEquals=" + DEFAULT_LAST_NAME);

        // Get all the clientList where lastName not equals to UPDATED_LAST_NAME
        defaultClientShouldBeFound("lastName.notEquals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllClientsByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where lastName in DEFAULT_LAST_NAME or UPDATED_LAST_NAME
        defaultClientShouldBeFound("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME);

        // Get all the clientList where lastName equals to UPDATED_LAST_NAME
        defaultClientShouldNotBeFound("lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllClientsByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where lastName is not null
        defaultClientShouldBeFound("lastName.specified=true");

        // Get all the clientList where lastName is null
        defaultClientShouldNotBeFound("lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllClientsByLastNameContainsSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where lastName contains DEFAULT_LAST_NAME
        defaultClientShouldBeFound("lastName.contains=" + DEFAULT_LAST_NAME);

        // Get all the clientList where lastName contains UPDATED_LAST_NAME
        defaultClientShouldNotBeFound("lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllClientsByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where lastName does not contain DEFAULT_LAST_NAME
        defaultClientShouldNotBeFound("lastName.doesNotContain=" + DEFAULT_LAST_NAME);

        // Get all the clientList where lastName does not contain UPDATED_LAST_NAME
        defaultClientShouldBeFound("lastName.doesNotContain=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllClientsByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where firstName equals to DEFAULT_FIRST_NAME
        defaultClientShouldBeFound("firstName.equals=" + DEFAULT_FIRST_NAME);

        // Get all the clientList where firstName equals to UPDATED_FIRST_NAME
        defaultClientShouldNotBeFound("firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllClientsByFirstNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where firstName not equals to DEFAULT_FIRST_NAME
        defaultClientShouldNotBeFound("firstName.notEquals=" + DEFAULT_FIRST_NAME);

        // Get all the clientList where firstName not equals to UPDATED_FIRST_NAME
        defaultClientShouldBeFound("firstName.notEquals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllClientsByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where firstName in DEFAULT_FIRST_NAME or UPDATED_FIRST_NAME
        defaultClientShouldBeFound("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME);

        // Get all the clientList where firstName equals to UPDATED_FIRST_NAME
        defaultClientShouldNotBeFound("firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllClientsByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where firstName is not null
        defaultClientShouldBeFound("firstName.specified=true");

        // Get all the clientList where firstName is null
        defaultClientShouldNotBeFound("firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllClientsByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where firstName contains DEFAULT_FIRST_NAME
        defaultClientShouldBeFound("firstName.contains=" + DEFAULT_FIRST_NAME);

        // Get all the clientList where firstName contains UPDATED_FIRST_NAME
        defaultClientShouldNotBeFound("firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllClientsByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where firstName does not contain DEFAULT_FIRST_NAME
        defaultClientShouldNotBeFound("firstName.doesNotContain=" + DEFAULT_FIRST_NAME);

        // Get all the clientList where firstName does not contain UPDATED_FIRST_NAME
        defaultClientShouldBeFound("firstName.doesNotContain=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllClientsByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where email equals to DEFAULT_EMAIL
        defaultClientShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the clientList where email equals to UPDATED_EMAIL
        defaultClientShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllClientsByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where email not equals to DEFAULT_EMAIL
        defaultClientShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the clientList where email not equals to UPDATED_EMAIL
        defaultClientShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllClientsByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultClientShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the clientList where email equals to UPDATED_EMAIL
        defaultClientShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllClientsByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where email is not null
        defaultClientShouldBeFound("email.specified=true");

        // Get all the clientList where email is null
        defaultClientShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    void getAllClientsByEmailContainsSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where email contains DEFAULT_EMAIL
        defaultClientShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the clientList where email contains UPDATED_EMAIL
        defaultClientShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllClientsByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where email does not contain DEFAULT_EMAIL
        defaultClientShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the clientList where email does not contain UPDATED_EMAIL
        defaultClientShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllClientsByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where phone equals to DEFAULT_PHONE
        defaultClientShouldBeFound("phone.equals=" + DEFAULT_PHONE);

        // Get all the clientList where phone equals to UPDATED_PHONE
        defaultClientShouldNotBeFound("phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllClientsByPhoneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where phone not equals to DEFAULT_PHONE
        defaultClientShouldNotBeFound("phone.notEquals=" + DEFAULT_PHONE);

        // Get all the clientList where phone not equals to UPDATED_PHONE
        defaultClientShouldBeFound("phone.notEquals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllClientsByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where phone in DEFAULT_PHONE or UPDATED_PHONE
        defaultClientShouldBeFound("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE);

        // Get all the clientList where phone equals to UPDATED_PHONE
        defaultClientShouldNotBeFound("phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllClientsByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where phone is not null
        defaultClientShouldBeFound("phone.specified=true");

        // Get all the clientList where phone is null
        defaultClientShouldNotBeFound("phone.specified=false");
    }

    @Test
    @Transactional
    void getAllClientsByPhoneContainsSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where phone contains DEFAULT_PHONE
        defaultClientShouldBeFound("phone.contains=" + DEFAULT_PHONE);

        // Get all the clientList where phone contains UPDATED_PHONE
        defaultClientShouldNotBeFound("phone.contains=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllClientsByPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where phone does not contain DEFAULT_PHONE
        defaultClientShouldNotBeFound("phone.doesNotContain=" + DEFAULT_PHONE);

        // Get all the clientList where phone does not contain UPDATED_PHONE
        defaultClientShouldBeFound("phone.doesNotContain=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllClientsByAdressIsEqualToSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where adress equals to DEFAULT_ADRESS
        defaultClientShouldBeFound("adress.equals=" + DEFAULT_ADRESS);

        // Get all the clientList where adress equals to UPDATED_ADRESS
        defaultClientShouldNotBeFound("adress.equals=" + UPDATED_ADRESS);
    }

    @Test
    @Transactional
    void getAllClientsByAdressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where adress not equals to DEFAULT_ADRESS
        defaultClientShouldNotBeFound("adress.notEquals=" + DEFAULT_ADRESS);

        // Get all the clientList where adress not equals to UPDATED_ADRESS
        defaultClientShouldBeFound("adress.notEquals=" + UPDATED_ADRESS);
    }

    @Test
    @Transactional
    void getAllClientsByAdressIsInShouldWork() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where adress in DEFAULT_ADRESS or UPDATED_ADRESS
        defaultClientShouldBeFound("adress.in=" + DEFAULT_ADRESS + "," + UPDATED_ADRESS);

        // Get all the clientList where adress equals to UPDATED_ADRESS
        defaultClientShouldNotBeFound("adress.in=" + UPDATED_ADRESS);
    }

    @Test
    @Transactional
    void getAllClientsByAdressIsNullOrNotNull() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where adress is not null
        defaultClientShouldBeFound("adress.specified=true");

        // Get all the clientList where adress is null
        defaultClientShouldNotBeFound("adress.specified=false");
    }

    @Test
    @Transactional
    void getAllClientsByAdressContainsSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where adress contains DEFAULT_ADRESS
        defaultClientShouldBeFound("adress.contains=" + DEFAULT_ADRESS);

        // Get all the clientList where adress contains UPDATED_ADRESS
        defaultClientShouldNotBeFound("adress.contains=" + UPDATED_ADRESS);
    }

    @Test
    @Transactional
    void getAllClientsByAdressNotContainsSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where adress does not contain DEFAULT_ADRESS
        defaultClientShouldNotBeFound("adress.doesNotContain=" + DEFAULT_ADRESS);

        // Get all the clientList where adress does not contain UPDATED_ADRESS
        defaultClientShouldBeFound("adress.doesNotContain=" + UPDATED_ADRESS);
    }

    @Test
    @Transactional
    void getAllClientsByCountryIsEqualToSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where country equals to DEFAULT_COUNTRY
        defaultClientShouldBeFound("country.equals=" + DEFAULT_COUNTRY);

        // Get all the clientList where country equals to UPDATED_COUNTRY
        defaultClientShouldNotBeFound("country.equals=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllClientsByCountryIsNotEqualToSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where country not equals to DEFAULT_COUNTRY
        defaultClientShouldNotBeFound("country.notEquals=" + DEFAULT_COUNTRY);

        // Get all the clientList where country not equals to UPDATED_COUNTRY
        defaultClientShouldBeFound("country.notEquals=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllClientsByCountryIsInShouldWork() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where country in DEFAULT_COUNTRY or UPDATED_COUNTRY
        defaultClientShouldBeFound("country.in=" + DEFAULT_COUNTRY + "," + UPDATED_COUNTRY);

        // Get all the clientList where country equals to UPDATED_COUNTRY
        defaultClientShouldNotBeFound("country.in=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllClientsByCountryIsNullOrNotNull() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where country is not null
        defaultClientShouldBeFound("country.specified=true");

        // Get all the clientList where country is null
        defaultClientShouldNotBeFound("country.specified=false");
    }

    @Test
    @Transactional
    void getAllClientsByCountryContainsSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where country contains DEFAULT_COUNTRY
        defaultClientShouldBeFound("country.contains=" + DEFAULT_COUNTRY);

        // Get all the clientList where country contains UPDATED_COUNTRY
        defaultClientShouldNotBeFound("country.contains=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllClientsByCountryNotContainsSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where country does not contain DEFAULT_COUNTRY
        defaultClientShouldNotBeFound("country.doesNotContain=" + DEFAULT_COUNTRY);

        // Get all the clientList where country does not contain UPDATED_COUNTRY
        defaultClientShouldBeFound("country.doesNotContain=" + UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void getAllClientsByPostalCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where postalCode equals to DEFAULT_POSTAL_CODE
        defaultClientShouldBeFound("postalCode.equals=" + DEFAULT_POSTAL_CODE);

        // Get all the clientList where postalCode equals to UPDATED_POSTAL_CODE
        defaultClientShouldNotBeFound("postalCode.equals=" + UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    void getAllClientsByPostalCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where postalCode not equals to DEFAULT_POSTAL_CODE
        defaultClientShouldNotBeFound("postalCode.notEquals=" + DEFAULT_POSTAL_CODE);

        // Get all the clientList where postalCode not equals to UPDATED_POSTAL_CODE
        defaultClientShouldBeFound("postalCode.notEquals=" + UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    void getAllClientsByPostalCodeIsInShouldWork() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where postalCode in DEFAULT_POSTAL_CODE or UPDATED_POSTAL_CODE
        defaultClientShouldBeFound("postalCode.in=" + DEFAULT_POSTAL_CODE + "," + UPDATED_POSTAL_CODE);

        // Get all the clientList where postalCode equals to UPDATED_POSTAL_CODE
        defaultClientShouldNotBeFound("postalCode.in=" + UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    void getAllClientsByPostalCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where postalCode is not null
        defaultClientShouldBeFound("postalCode.specified=true");

        // Get all the clientList where postalCode is null
        defaultClientShouldNotBeFound("postalCode.specified=false");
    }

    @Test
    @Transactional
    void getAllClientsByPostalCodeContainsSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where postalCode contains DEFAULT_POSTAL_CODE
        defaultClientShouldBeFound("postalCode.contains=" + DEFAULT_POSTAL_CODE);

        // Get all the clientList where postalCode contains UPDATED_POSTAL_CODE
        defaultClientShouldNotBeFound("postalCode.contains=" + UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    void getAllClientsByPostalCodeNotContainsSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        // Get all the clientList where postalCode does not contain DEFAULT_POSTAL_CODE
        defaultClientShouldNotBeFound("postalCode.doesNotContain=" + DEFAULT_POSTAL_CODE);

        // Get all the clientList where postalCode does not contain UPDATED_POSTAL_CODE
        defaultClientShouldBeFound("postalCode.doesNotContain=" + UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    void getAllClientsByConnectionIsEqualToSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);
        Connection connection;
        if (TestUtil.findAll(em, Connection.class).isEmpty()) {
            connection = ConnectionResourceIT.createEntity(em);
            em.persist(connection);
            em.flush();
        } else {
            connection = TestUtil.findAll(em, Connection.class).get(0);
        }
        em.persist(connection);
        em.flush();
        client.setConnection(connection);
        clientRepository.saveAndFlush(client);
        Long connectionId = connection.getId();

        // Get all the clientList where connection equals to connectionId
        defaultClientShouldBeFound("connectionId.equals=" + connectionId);

        // Get all the clientList where connection equals to (connectionId + 1)
        defaultClientShouldNotBeFound("connectionId.equals=" + (connectionId + 1));
    }

    @Test
    @Transactional
    void getAllClientsByOrderIsEqualToSomething() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);
        Order order;
        if (TestUtil.findAll(em, Order.class).isEmpty()) {
            order = OrderResourceIT.createEntity(em);
            em.persist(order);
            em.flush();
        } else {
            order = TestUtil.findAll(em, Order.class).get(0);
        }
        em.persist(order);
        em.flush();
        client.addOrder(order);
        clientRepository.saveAndFlush(client);
        Long orderId = order.getId();

        // Get all the clientList where order equals to orderId
        defaultClientShouldBeFound("orderId.equals=" + orderId);

        // Get all the clientList where order equals to (orderId + 1)
        defaultClientShouldNotBeFound("orderId.equals=" + (orderId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultClientShouldBeFound(String filter) throws Exception {
        restClientMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(client.getId().intValue())))
            .andExpect(jsonPath("$.[*].addedDateTime").value(hasItem(sameInstant(DEFAULT_ADDED_DATE_TIME))))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].adress").value(hasItem(DEFAULT_ADRESS)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)))
            .andExpect(jsonPath("$.[*].postalCode").value(hasItem(DEFAULT_POSTAL_CODE)));

        // Check, that the count call also returns 1
        restClientMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultClientShouldNotBeFound(String filter) throws Exception {
        restClientMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restClientMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingClient() throws Exception {
        // Get the client
        restClientMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewClient() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        int databaseSizeBeforeUpdate = clientRepository.findAll().size();

        // Update the client
        Client updatedClient = clientRepository.findById(client.getId()).get();
        // Disconnect from session so that the updates on updatedClient are not directly saved in db
        em.detach(updatedClient);
        updatedClient
            .addedDateTime(UPDATED_ADDED_DATE_TIME)
            .lastName(UPDATED_LAST_NAME)
            .firstName(UPDATED_FIRST_NAME)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .adress(UPDATED_ADRESS)
            .country(UPDATED_COUNTRY)
            .postalCode(UPDATED_POSTAL_CODE);

        restClientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedClient.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedClient))
            )
            .andExpect(status().isOk());

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeUpdate);
        Client testClient = clientList.get(clientList.size() - 1);
        assertThat(testClient.getAddedDateTime()).isEqualTo(UPDATED_ADDED_DATE_TIME);
        assertThat(testClient.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testClient.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testClient.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testClient.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testClient.getAdress()).isEqualTo(UPDATED_ADRESS);
        assertThat(testClient.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testClient.getPostalCode()).isEqualTo(UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    void putNonExistingClient() throws Exception {
        int databaseSizeBeforeUpdate = clientRepository.findAll().size();
        client.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, client.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(client))
            )
            .andExpect(status().isBadRequest());

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchClient() throws Exception {
        int databaseSizeBeforeUpdate = clientRepository.findAll().size();
        client.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(client))
            )
            .andExpect(status().isBadRequest());

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamClient() throws Exception {
        int databaseSizeBeforeUpdate = clientRepository.findAll().size();
        client.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(client)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateClientWithPatch() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        int databaseSizeBeforeUpdate = clientRepository.findAll().size();

        // Update the client using partial update
        Client partialUpdatedClient = new Client();
        partialUpdatedClient.setId(client.getId());

        partialUpdatedClient
            .addedDateTime(UPDATED_ADDED_DATE_TIME)
            .lastName(UPDATED_LAST_NAME)
            .firstName(UPDATED_FIRST_NAME)
            .country(UPDATED_COUNTRY);

        restClientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClient.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedClient))
            )
            .andExpect(status().isOk());

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeUpdate);
        Client testClient = clientList.get(clientList.size() - 1);
        assertThat(testClient.getAddedDateTime()).isEqualTo(UPDATED_ADDED_DATE_TIME);
        assertThat(testClient.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testClient.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testClient.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testClient.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testClient.getAdress()).isEqualTo(DEFAULT_ADRESS);
        assertThat(testClient.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testClient.getPostalCode()).isEqualTo(DEFAULT_POSTAL_CODE);
    }

    @Test
    @Transactional
    void fullUpdateClientWithPatch() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        int databaseSizeBeforeUpdate = clientRepository.findAll().size();

        // Update the client using partial update
        Client partialUpdatedClient = new Client();
        partialUpdatedClient.setId(client.getId());

        partialUpdatedClient
            .addedDateTime(UPDATED_ADDED_DATE_TIME)
            .lastName(UPDATED_LAST_NAME)
            .firstName(UPDATED_FIRST_NAME)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .adress(UPDATED_ADRESS)
            .country(UPDATED_COUNTRY)
            .postalCode(UPDATED_POSTAL_CODE);

        restClientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClient.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedClient))
            )
            .andExpect(status().isOk());

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeUpdate);
        Client testClient = clientList.get(clientList.size() - 1);
        assertThat(testClient.getAddedDateTime()).isEqualTo(UPDATED_ADDED_DATE_TIME);
        assertThat(testClient.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testClient.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testClient.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testClient.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testClient.getAdress()).isEqualTo(UPDATED_ADRESS);
        assertThat(testClient.getCountry()).isEqualTo(UPDATED_COUNTRY);
        assertThat(testClient.getPostalCode()).isEqualTo(UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    void patchNonExistingClient() throws Exception {
        int databaseSizeBeforeUpdate = clientRepository.findAll().size();
        client.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, client.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(client))
            )
            .andExpect(status().isBadRequest());

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchClient() throws Exception {
        int databaseSizeBeforeUpdate = clientRepository.findAll().size();
        client.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(client))
            )
            .andExpect(status().isBadRequest());

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamClient() throws Exception {
        int databaseSizeBeforeUpdate = clientRepository.findAll().size();
        client.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClientMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(client)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Client in the database
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteClient() throws Exception {
        // Initialize the database
        clientRepository.saveAndFlush(client);

        int databaseSizeBeforeDelete = clientRepository.findAll().size();

        // Delete the client
        restClientMockMvc
            .perform(delete(ENTITY_API_URL_ID, client.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Client> clientList = clientRepository.findAll();
        assertThat(clientList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
