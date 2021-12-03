package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Product;
import com.mycompany.myapp.domain.Seller;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.repository.SellerRepository;
import com.mycompany.myapp.service.criteria.SellerCriteria;
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
 * Integration tests for the {@link SellerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SellerResourceIT {

    private static final String DEFAULT_SOCIAL_REASON = "AAAAAAAAAA";
    private static final String UPDATED_SOCIAL_REASON = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_SIRET_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_SIRET_NUMBER = "BBBBBBBBBB";

    private static final Long DEFAULT_PHONE = 1L;
    private static final Long UPDATED_PHONE = 2L;
    private static final Long SMALLER_PHONE = 1L - 1L;

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/sellers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSellerMockMvc;

    private Seller seller;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Seller createEntity(EntityManager em) {
        Seller seller = new Seller()
            .socialReason(DEFAULT_SOCIAL_REASON)
            .address(DEFAULT_ADDRESS)
            .siretNumber(DEFAULT_SIRET_NUMBER)
            .phone(DEFAULT_PHONE)
            .email(DEFAULT_EMAIL);
        return seller;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Seller createUpdatedEntity(EntityManager em) {
        Seller seller = new Seller()
            .socialReason(UPDATED_SOCIAL_REASON)
            .address(UPDATED_ADDRESS)
            .siretNumber(UPDATED_SIRET_NUMBER)
            .phone(UPDATED_PHONE)
            .email(UPDATED_EMAIL);
        return seller;
    }

    @BeforeEach
    public void initTest() {
        seller = createEntity(em);
    }

    @Test
    @Transactional
    void createSeller() throws Exception {
        int databaseSizeBeforeCreate = sellerRepository.findAll().size();
        // Create the Seller
        restSellerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(seller)))
            .andExpect(status().isCreated());

        // Validate the Seller in the database
        List<Seller> sellerList = sellerRepository.findAll();
        assertThat(sellerList).hasSize(databaseSizeBeforeCreate + 1);
        Seller testSeller = sellerList.get(sellerList.size() - 1);
        assertThat(testSeller.getSocialReason()).isEqualTo(DEFAULT_SOCIAL_REASON);
        assertThat(testSeller.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testSeller.getSiretNumber()).isEqualTo(DEFAULT_SIRET_NUMBER);
        assertThat(testSeller.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testSeller.getEmail()).isEqualTo(DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void createSellerWithExistingId() throws Exception {
        // Create the Seller with an existing ID
        seller.setId(1L);

        int databaseSizeBeforeCreate = sellerRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSellerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(seller)))
            .andExpect(status().isBadRequest());

        // Validate the Seller in the database
        List<Seller> sellerList = sellerRepository.findAll();
        assertThat(sellerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSocialReasonIsRequired() throws Exception {
        int databaseSizeBeforeTest = sellerRepository.findAll().size();
        // set the field null
        seller.setSocialReason(null);

        // Create the Seller, which fails.

        restSellerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(seller)))
            .andExpect(status().isBadRequest());

        List<Seller> sellerList = sellerRepository.findAll();
        assertThat(sellerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = sellerRepository.findAll().size();
        // set the field null
        seller.setAddress(null);

        // Create the Seller, which fails.

        restSellerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(seller)))
            .andExpect(status().isBadRequest());

        List<Seller> sellerList = sellerRepository.findAll();
        assertThat(sellerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = sellerRepository.findAll().size();
        // set the field null
        seller.setEmail(null);

        // Create the Seller, which fails.

        restSellerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(seller)))
            .andExpect(status().isBadRequest());

        List<Seller> sellerList = sellerRepository.findAll();
        assertThat(sellerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSellers() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        // Get all the sellerList
        restSellerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(seller.getId().intValue())))
            .andExpect(jsonPath("$.[*].socialReason").value(hasItem(DEFAULT_SOCIAL_REASON)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].siretNumber").value(hasItem(DEFAULT_SIRET_NUMBER)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.intValue())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)));
    }

    @Test
    @Transactional
    void getSeller() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        // Get the seller
        restSellerMockMvc
            .perform(get(ENTITY_API_URL_ID, seller.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(seller.getId().intValue()))
            .andExpect(jsonPath("$.socialReason").value(DEFAULT_SOCIAL_REASON))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.siretNumber").value(DEFAULT_SIRET_NUMBER))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE.intValue()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL));
    }

    @Test
    @Transactional
    void getSellersByIdFiltering() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        Long id = seller.getId();

        defaultSellerShouldBeFound("id.equals=" + id);
        defaultSellerShouldNotBeFound("id.notEquals=" + id);

        defaultSellerShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultSellerShouldNotBeFound("id.greaterThan=" + id);

        defaultSellerShouldBeFound("id.lessThanOrEqual=" + id);
        defaultSellerShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSellersBySocialReasonIsEqualToSomething() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        // Get all the sellerList where socialReason equals to DEFAULT_SOCIAL_REASON
        defaultSellerShouldBeFound("socialReason.equals=" + DEFAULT_SOCIAL_REASON);

        // Get all the sellerList where socialReason equals to UPDATED_SOCIAL_REASON
        defaultSellerShouldNotBeFound("socialReason.equals=" + UPDATED_SOCIAL_REASON);
    }

    @Test
    @Transactional
    void getAllSellersBySocialReasonIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        // Get all the sellerList where socialReason not equals to DEFAULT_SOCIAL_REASON
        defaultSellerShouldNotBeFound("socialReason.notEquals=" + DEFAULT_SOCIAL_REASON);

        // Get all the sellerList where socialReason not equals to UPDATED_SOCIAL_REASON
        defaultSellerShouldBeFound("socialReason.notEquals=" + UPDATED_SOCIAL_REASON);
    }

    @Test
    @Transactional
    void getAllSellersBySocialReasonIsInShouldWork() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        // Get all the sellerList where socialReason in DEFAULT_SOCIAL_REASON or UPDATED_SOCIAL_REASON
        defaultSellerShouldBeFound("socialReason.in=" + DEFAULT_SOCIAL_REASON + "," + UPDATED_SOCIAL_REASON);

        // Get all the sellerList where socialReason equals to UPDATED_SOCIAL_REASON
        defaultSellerShouldNotBeFound("socialReason.in=" + UPDATED_SOCIAL_REASON);
    }

    @Test
    @Transactional
    void getAllSellersBySocialReasonIsNullOrNotNull() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        // Get all the sellerList where socialReason is not null
        defaultSellerShouldBeFound("socialReason.specified=true");

        // Get all the sellerList where socialReason is null
        defaultSellerShouldNotBeFound("socialReason.specified=false");
    }

    @Test
    @Transactional
    void getAllSellersBySocialReasonContainsSomething() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        // Get all the sellerList where socialReason contains DEFAULT_SOCIAL_REASON
        defaultSellerShouldBeFound("socialReason.contains=" + DEFAULT_SOCIAL_REASON);

        // Get all the sellerList where socialReason contains UPDATED_SOCIAL_REASON
        defaultSellerShouldNotBeFound("socialReason.contains=" + UPDATED_SOCIAL_REASON);
    }

    @Test
    @Transactional
    void getAllSellersBySocialReasonNotContainsSomething() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        // Get all the sellerList where socialReason does not contain DEFAULT_SOCIAL_REASON
        defaultSellerShouldNotBeFound("socialReason.doesNotContain=" + DEFAULT_SOCIAL_REASON);

        // Get all the sellerList where socialReason does not contain UPDATED_SOCIAL_REASON
        defaultSellerShouldBeFound("socialReason.doesNotContain=" + UPDATED_SOCIAL_REASON);
    }

    @Test
    @Transactional
    void getAllSellersByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        // Get all the sellerList where address equals to DEFAULT_ADDRESS
        defaultSellerShouldBeFound("address.equals=" + DEFAULT_ADDRESS);

        // Get all the sellerList where address equals to UPDATED_ADDRESS
        defaultSellerShouldNotBeFound("address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllSellersByAddressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        // Get all the sellerList where address not equals to DEFAULT_ADDRESS
        defaultSellerShouldNotBeFound("address.notEquals=" + DEFAULT_ADDRESS);

        // Get all the sellerList where address not equals to UPDATED_ADDRESS
        defaultSellerShouldBeFound("address.notEquals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllSellersByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        // Get all the sellerList where address in DEFAULT_ADDRESS or UPDATED_ADDRESS
        defaultSellerShouldBeFound("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS);

        // Get all the sellerList where address equals to UPDATED_ADDRESS
        defaultSellerShouldNotBeFound("address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllSellersByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        // Get all the sellerList where address is not null
        defaultSellerShouldBeFound("address.specified=true");

        // Get all the sellerList where address is null
        defaultSellerShouldNotBeFound("address.specified=false");
    }

    @Test
    @Transactional
    void getAllSellersByAddressContainsSomething() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        // Get all the sellerList where address contains DEFAULT_ADDRESS
        defaultSellerShouldBeFound("address.contains=" + DEFAULT_ADDRESS);

        // Get all the sellerList where address contains UPDATED_ADDRESS
        defaultSellerShouldNotBeFound("address.contains=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllSellersByAddressNotContainsSomething() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        // Get all the sellerList where address does not contain DEFAULT_ADDRESS
        defaultSellerShouldNotBeFound("address.doesNotContain=" + DEFAULT_ADDRESS);

        // Get all the sellerList where address does not contain UPDATED_ADDRESS
        defaultSellerShouldBeFound("address.doesNotContain=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllSellersBySiretNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        // Get all the sellerList where siretNumber equals to DEFAULT_SIRET_NUMBER
        defaultSellerShouldBeFound("siretNumber.equals=" + DEFAULT_SIRET_NUMBER);

        // Get all the sellerList where siretNumber equals to UPDATED_SIRET_NUMBER
        defaultSellerShouldNotBeFound("siretNumber.equals=" + UPDATED_SIRET_NUMBER);
    }

    @Test
    @Transactional
    void getAllSellersBySiretNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        // Get all the sellerList where siretNumber not equals to DEFAULT_SIRET_NUMBER
        defaultSellerShouldNotBeFound("siretNumber.notEquals=" + DEFAULT_SIRET_NUMBER);

        // Get all the sellerList where siretNumber not equals to UPDATED_SIRET_NUMBER
        defaultSellerShouldBeFound("siretNumber.notEquals=" + UPDATED_SIRET_NUMBER);
    }

    @Test
    @Transactional
    void getAllSellersBySiretNumberIsInShouldWork() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        // Get all the sellerList where siretNumber in DEFAULT_SIRET_NUMBER or UPDATED_SIRET_NUMBER
        defaultSellerShouldBeFound("siretNumber.in=" + DEFAULT_SIRET_NUMBER + "," + UPDATED_SIRET_NUMBER);

        // Get all the sellerList where siretNumber equals to UPDATED_SIRET_NUMBER
        defaultSellerShouldNotBeFound("siretNumber.in=" + UPDATED_SIRET_NUMBER);
    }

    @Test
    @Transactional
    void getAllSellersBySiretNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        // Get all the sellerList where siretNumber is not null
        defaultSellerShouldBeFound("siretNumber.specified=true");

        // Get all the sellerList where siretNumber is null
        defaultSellerShouldNotBeFound("siretNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllSellersBySiretNumberContainsSomething() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        // Get all the sellerList where siretNumber contains DEFAULT_SIRET_NUMBER
        defaultSellerShouldBeFound("siretNumber.contains=" + DEFAULT_SIRET_NUMBER);

        // Get all the sellerList where siretNumber contains UPDATED_SIRET_NUMBER
        defaultSellerShouldNotBeFound("siretNumber.contains=" + UPDATED_SIRET_NUMBER);
    }

    @Test
    @Transactional
    void getAllSellersBySiretNumberNotContainsSomething() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        // Get all the sellerList where siretNumber does not contain DEFAULT_SIRET_NUMBER
        defaultSellerShouldNotBeFound("siretNumber.doesNotContain=" + DEFAULT_SIRET_NUMBER);

        // Get all the sellerList where siretNumber does not contain UPDATED_SIRET_NUMBER
        defaultSellerShouldBeFound("siretNumber.doesNotContain=" + UPDATED_SIRET_NUMBER);
    }

    @Test
    @Transactional
    void getAllSellersByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        // Get all the sellerList where phone equals to DEFAULT_PHONE
        defaultSellerShouldBeFound("phone.equals=" + DEFAULT_PHONE);

        // Get all the sellerList where phone equals to UPDATED_PHONE
        defaultSellerShouldNotBeFound("phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllSellersByPhoneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        // Get all the sellerList where phone not equals to DEFAULT_PHONE
        defaultSellerShouldNotBeFound("phone.notEquals=" + DEFAULT_PHONE);

        // Get all the sellerList where phone not equals to UPDATED_PHONE
        defaultSellerShouldBeFound("phone.notEquals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllSellersByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        // Get all the sellerList where phone in DEFAULT_PHONE or UPDATED_PHONE
        defaultSellerShouldBeFound("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE);

        // Get all the sellerList where phone equals to UPDATED_PHONE
        defaultSellerShouldNotBeFound("phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllSellersByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        // Get all the sellerList where phone is not null
        defaultSellerShouldBeFound("phone.specified=true");

        // Get all the sellerList where phone is null
        defaultSellerShouldNotBeFound("phone.specified=false");
    }

    @Test
    @Transactional
    void getAllSellersByPhoneIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        // Get all the sellerList where phone is greater than or equal to DEFAULT_PHONE
        defaultSellerShouldBeFound("phone.greaterThanOrEqual=" + DEFAULT_PHONE);

        // Get all the sellerList where phone is greater than or equal to UPDATED_PHONE
        defaultSellerShouldNotBeFound("phone.greaterThanOrEqual=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllSellersByPhoneIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        // Get all the sellerList where phone is less than or equal to DEFAULT_PHONE
        defaultSellerShouldBeFound("phone.lessThanOrEqual=" + DEFAULT_PHONE);

        // Get all the sellerList where phone is less than or equal to SMALLER_PHONE
        defaultSellerShouldNotBeFound("phone.lessThanOrEqual=" + SMALLER_PHONE);
    }

    @Test
    @Transactional
    void getAllSellersByPhoneIsLessThanSomething() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        // Get all the sellerList where phone is less than DEFAULT_PHONE
        defaultSellerShouldNotBeFound("phone.lessThan=" + DEFAULT_PHONE);

        // Get all the sellerList where phone is less than UPDATED_PHONE
        defaultSellerShouldBeFound("phone.lessThan=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllSellersByPhoneIsGreaterThanSomething() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        // Get all the sellerList where phone is greater than DEFAULT_PHONE
        defaultSellerShouldNotBeFound("phone.greaterThan=" + DEFAULT_PHONE);

        // Get all the sellerList where phone is greater than SMALLER_PHONE
        defaultSellerShouldBeFound("phone.greaterThan=" + SMALLER_PHONE);
    }

    @Test
    @Transactional
    void getAllSellersByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        // Get all the sellerList where email equals to DEFAULT_EMAIL
        defaultSellerShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the sellerList where email equals to UPDATED_EMAIL
        defaultSellerShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllSellersByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        // Get all the sellerList where email not equals to DEFAULT_EMAIL
        defaultSellerShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the sellerList where email not equals to UPDATED_EMAIL
        defaultSellerShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllSellersByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        // Get all the sellerList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultSellerShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the sellerList where email equals to UPDATED_EMAIL
        defaultSellerShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllSellersByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        // Get all the sellerList where email is not null
        defaultSellerShouldBeFound("email.specified=true");

        // Get all the sellerList where email is null
        defaultSellerShouldNotBeFound("email.specified=false");
    }

    @Test
    @Transactional
    void getAllSellersByEmailContainsSomething() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        // Get all the sellerList where email contains DEFAULT_EMAIL
        defaultSellerShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the sellerList where email contains UPDATED_EMAIL
        defaultSellerShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllSellersByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        // Get all the sellerList where email does not contain DEFAULT_EMAIL
        defaultSellerShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the sellerList where email does not contain UPDATED_EMAIL
        defaultSellerShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllSellersByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);
        User user;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            user = UserResourceIT.createEntity(em);
            em.persist(user);
            em.flush();
        } else {
            user = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(user);
        em.flush();
        seller.setUser(user);
        sellerRepository.saveAndFlush(seller);
        Long userId = user.getId();

        // Get all the sellerList where user equals to userId
        defaultSellerShouldBeFound("userId.equals=" + userId);

        // Get all the sellerList where user equals to (userId + 1)
        defaultSellerShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    @Test
    @Transactional
    void getAllSellersByProductIsEqualToSomething() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);
        Product product;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            product = ProductResourceIT.createEntity(em);
            em.persist(product);
            em.flush();
        } else {
            product = TestUtil.findAll(em, Product.class).get(0);
        }
        em.persist(product);
        em.flush();
        seller.addProduct(product);
        sellerRepository.saveAndFlush(seller);
        Long productId = product.getId();

        // Get all the sellerList where product equals to productId
        defaultSellerShouldBeFound("productId.equals=" + productId);

        // Get all the sellerList where product equals to (productId + 1)
        defaultSellerShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSellerShouldBeFound(String filter) throws Exception {
        restSellerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(seller.getId().intValue())))
            .andExpect(jsonPath("$.[*].socialReason").value(hasItem(DEFAULT_SOCIAL_REASON)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].siretNumber").value(hasItem(DEFAULT_SIRET_NUMBER)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE.intValue())))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)));

        // Check, that the count call also returns 1
        restSellerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSellerShouldNotBeFound(String filter) throws Exception {
        restSellerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSellerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSeller() throws Exception {
        // Get the seller
        restSellerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSeller() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        int databaseSizeBeforeUpdate = sellerRepository.findAll().size();

        // Update the seller
        Seller updatedSeller = sellerRepository.findById(seller.getId()).get();
        // Disconnect from session so that the updates on updatedSeller are not directly saved in db
        em.detach(updatedSeller);
        updatedSeller
            .socialReason(UPDATED_SOCIAL_REASON)
            .address(UPDATED_ADDRESS)
            .siretNumber(UPDATED_SIRET_NUMBER)
            .phone(UPDATED_PHONE)
            .email(UPDATED_EMAIL);

        restSellerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSeller.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSeller))
            )
            .andExpect(status().isOk());

        // Validate the Seller in the database
        List<Seller> sellerList = sellerRepository.findAll();
        assertThat(sellerList).hasSize(databaseSizeBeforeUpdate);
        Seller testSeller = sellerList.get(sellerList.size() - 1);
        assertThat(testSeller.getSocialReason()).isEqualTo(UPDATED_SOCIAL_REASON);
        assertThat(testSeller.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testSeller.getSiretNumber()).isEqualTo(UPDATED_SIRET_NUMBER);
        assertThat(testSeller.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testSeller.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void putNonExistingSeller() throws Exception {
        int databaseSizeBeforeUpdate = sellerRepository.findAll().size();
        seller.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSellerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, seller.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(seller))
            )
            .andExpect(status().isBadRequest());

        // Validate the Seller in the database
        List<Seller> sellerList = sellerRepository.findAll();
        assertThat(sellerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSeller() throws Exception {
        int databaseSizeBeforeUpdate = sellerRepository.findAll().size();
        seller.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSellerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(seller))
            )
            .andExpect(status().isBadRequest());

        // Validate the Seller in the database
        List<Seller> sellerList = sellerRepository.findAll();
        assertThat(sellerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSeller() throws Exception {
        int databaseSizeBeforeUpdate = sellerRepository.findAll().size();
        seller.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSellerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(seller)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Seller in the database
        List<Seller> sellerList = sellerRepository.findAll();
        assertThat(sellerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSellerWithPatch() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        int databaseSizeBeforeUpdate = sellerRepository.findAll().size();

        // Update the seller using partial update
        Seller partialUpdatedSeller = new Seller();
        partialUpdatedSeller.setId(seller.getId());

        partialUpdatedSeller.phone(UPDATED_PHONE);

        restSellerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSeller.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSeller))
            )
            .andExpect(status().isOk());

        // Validate the Seller in the database
        List<Seller> sellerList = sellerRepository.findAll();
        assertThat(sellerList).hasSize(databaseSizeBeforeUpdate);
        Seller testSeller = sellerList.get(sellerList.size() - 1);
        assertThat(testSeller.getSocialReason()).isEqualTo(DEFAULT_SOCIAL_REASON);
        assertThat(testSeller.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testSeller.getSiretNumber()).isEqualTo(DEFAULT_SIRET_NUMBER);
        assertThat(testSeller.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testSeller.getEmail()).isEqualTo(DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void fullUpdateSellerWithPatch() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        int databaseSizeBeforeUpdate = sellerRepository.findAll().size();

        // Update the seller using partial update
        Seller partialUpdatedSeller = new Seller();
        partialUpdatedSeller.setId(seller.getId());

        partialUpdatedSeller
            .socialReason(UPDATED_SOCIAL_REASON)
            .address(UPDATED_ADDRESS)
            .siretNumber(UPDATED_SIRET_NUMBER)
            .phone(UPDATED_PHONE)
            .email(UPDATED_EMAIL);

        restSellerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSeller.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSeller))
            )
            .andExpect(status().isOk());

        // Validate the Seller in the database
        List<Seller> sellerList = sellerRepository.findAll();
        assertThat(sellerList).hasSize(databaseSizeBeforeUpdate);
        Seller testSeller = sellerList.get(sellerList.size() - 1);
        assertThat(testSeller.getSocialReason()).isEqualTo(UPDATED_SOCIAL_REASON);
        assertThat(testSeller.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testSeller.getSiretNumber()).isEqualTo(UPDATED_SIRET_NUMBER);
        assertThat(testSeller.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testSeller.getEmail()).isEqualTo(UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void patchNonExistingSeller() throws Exception {
        int databaseSizeBeforeUpdate = sellerRepository.findAll().size();
        seller.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSellerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, seller.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(seller))
            )
            .andExpect(status().isBadRequest());

        // Validate the Seller in the database
        List<Seller> sellerList = sellerRepository.findAll();
        assertThat(sellerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSeller() throws Exception {
        int databaseSizeBeforeUpdate = sellerRepository.findAll().size();
        seller.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSellerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(seller))
            )
            .andExpect(status().isBadRequest());

        // Validate the Seller in the database
        List<Seller> sellerList = sellerRepository.findAll();
        assertThat(sellerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSeller() throws Exception {
        int databaseSizeBeforeUpdate = sellerRepository.findAll().size();
        seller.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSellerMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(seller)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Seller in the database
        List<Seller> sellerList = sellerRepository.findAll();
        assertThat(sellerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSeller() throws Exception {
        // Initialize the database
        sellerRepository.saveAndFlush(seller);

        int databaseSizeBeforeDelete = sellerRepository.findAll().size();

        // Delete the seller
        restSellerMockMvc
            .perform(delete(ENTITY_API_URL_ID, seller.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Seller> sellerList = sellerRepository.findAll();
        assertThat(sellerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
