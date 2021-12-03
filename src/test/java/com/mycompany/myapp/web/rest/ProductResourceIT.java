package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Brand;
import com.mycompany.myapp.domain.Order;
import com.mycompany.myapp.domain.Product;
import com.mycompany.myapp.domain.Seller;
import com.mycompany.myapp.domain.SubFamily;
import com.mycompany.myapp.domain.Tag;
import com.mycompany.myapp.domain.enumeration.Color;
import com.mycompany.myapp.repository.ProductRepository;
import com.mycompany.myapp.service.ProductService;
import com.mycompany.myapp.service.criteria.ProductCriteria;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ProductResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ProductResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Long DEFAULT_PHOTO_ID = 1L;
    private static final Long UPDATED_PHOTO_ID = 2L;
    private static final Long SMALLER_PHOTO_ID = 1L - 1L;

    private static final Long DEFAULT_STOCK = 1L;
    private static final Long UPDATED_STOCK = 2L;
    private static final Long SMALLER_STOCK = 1L - 1L;

    private static final Double DEFAULT_PRICE = 1D;
    private static final Double UPDATED_PRICE = 2D;
    private static final Double SMALLER_PRICE = 1D - 1D;

    private static final String DEFAULT_MODEL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_MODEL_NAME = "BBBBBBBBBB";

    private static final Color DEFAULT_COLOR = Color.PURPLE;
    private static final Color UPDATED_COLOR = Color.BLUE;

    private static final String ENTITY_API_URL = "/api/products";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProductRepository productRepository;

    @Mock
    private ProductRepository productRepositoryMock;

    @Mock
    private ProductService productServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductMockMvc;

    private Product product;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Product createEntity(EntityManager em) {
        Product product = new Product()
            .description(DEFAULT_DESCRIPTION)
            .photoId(DEFAULT_PHOTO_ID)
            .stock(DEFAULT_STOCK)
            .price(DEFAULT_PRICE)
            .modelName(DEFAULT_MODEL_NAME)
            .color(DEFAULT_COLOR);
        // Add required entity
        SubFamily subFamily;
        if (TestUtil.findAll(em, SubFamily.class).isEmpty()) {
            subFamily = SubFamilyResourceIT.createEntity(em);
            em.persist(subFamily);
            em.flush();
        } else {
            subFamily = TestUtil.findAll(em, SubFamily.class).get(0);
        }
        product.setSubFamily(subFamily);
        // Add required entity
        Brand brand;
        if (TestUtil.findAll(em, Brand.class).isEmpty()) {
            brand = BrandResourceIT.createEntity(em);
            em.persist(brand);
            em.flush();
        } else {
            brand = TestUtil.findAll(em, Brand.class).get(0);
        }
        product.setBrand(brand);
        // Add required entity
        Seller seller;
        if (TestUtil.findAll(em, Seller.class).isEmpty()) {
            seller = SellerResourceIT.createEntity(em);
            em.persist(seller);
            em.flush();
        } else {
            seller = TestUtil.findAll(em, Seller.class).get(0);
        }
        product.setSeller(seller);
        return product;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Product createUpdatedEntity(EntityManager em) {
        Product product = new Product()
            .description(UPDATED_DESCRIPTION)
            .photoId(UPDATED_PHOTO_ID)
            .stock(UPDATED_STOCK)
            .price(UPDATED_PRICE)
            .modelName(UPDATED_MODEL_NAME)
            .color(UPDATED_COLOR);
        // Add required entity
        SubFamily subFamily;
        if (TestUtil.findAll(em, SubFamily.class).isEmpty()) {
            subFamily = SubFamilyResourceIT.createUpdatedEntity(em);
            em.persist(subFamily);
            em.flush();
        } else {
            subFamily = TestUtil.findAll(em, SubFamily.class).get(0);
        }
        product.setSubFamily(subFamily);
        // Add required entity
        Brand brand;
        if (TestUtil.findAll(em, Brand.class).isEmpty()) {
            brand = BrandResourceIT.createUpdatedEntity(em);
            em.persist(brand);
            em.flush();
        } else {
            brand = TestUtil.findAll(em, Brand.class).get(0);
        }
        product.setBrand(brand);
        // Add required entity
        Seller seller;
        if (TestUtil.findAll(em, Seller.class).isEmpty()) {
            seller = SellerResourceIT.createUpdatedEntity(em);
            em.persist(seller);
            em.flush();
        } else {
            seller = TestUtil.findAll(em, Seller.class).get(0);
        }
        product.setSeller(seller);
        return product;
    }

    @BeforeEach
    public void initTest() {
        product = createEntity(em);
    }

    @Test
    @Transactional
    void createProduct() throws Exception {
        int databaseSizeBeforeCreate = productRepository.findAll().size();
        // Create the Product
        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(product)))
            .andExpect(status().isCreated());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeCreate + 1);
        Product testProduct = productList.get(productList.size() - 1);
        assertThat(testProduct.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProduct.getPhotoId()).isEqualTo(DEFAULT_PHOTO_ID);
        assertThat(testProduct.getStock()).isEqualTo(DEFAULT_STOCK);
        assertThat(testProduct.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testProduct.getModelName()).isEqualTo(DEFAULT_MODEL_NAME);
        assertThat(testProduct.getColor()).isEqualTo(DEFAULT_COLOR);
    }

    @Test
    @Transactional
    void createProductWithExistingId() throws Exception {
        // Create the Product with an existing ID
        product.setId(1L);

        int databaseSizeBeforeCreate = productRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(product)))
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStockIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().size();
        // set the field null
        product.setStock(null);

        // Create the Product, which fails.

        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(product)))
            .andExpect(status().isBadRequest());

        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().size();
        // set the field null
        product.setPrice(null);

        // Create the Product, which fails.

        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(product)))
            .andExpect(status().isBadRequest());

        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkModelNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().size();
        // set the field null
        product.setModelName(null);

        // Create the Product, which fails.

        restProductMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(product)))
            .andExpect(status().isBadRequest());

        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProducts() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList
        restProductMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(product.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].photoId").value(hasItem(DEFAULT_PHOTO_ID.intValue())))
            .andExpect(jsonPath("$.[*].stock").value(hasItem(DEFAULT_STOCK.intValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].modelName").value(hasItem(DEFAULT_MODEL_NAME)))
            .andExpect(jsonPath("$.[*].color").value(hasItem(DEFAULT_COLOR.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductsWithEagerRelationshipsIsEnabled() throws Exception {
        when(productServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(productServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(productServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(productServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getProduct() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get the product
        restProductMockMvc
            .perform(get(ENTITY_API_URL_ID, product.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(product.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.photoId").value(DEFAULT_PHOTO_ID.intValue()))
            .andExpect(jsonPath("$.stock").value(DEFAULT_STOCK.intValue()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.modelName").value(DEFAULT_MODEL_NAME))
            .andExpect(jsonPath("$.color").value(DEFAULT_COLOR.toString()));
    }

    @Test
    @Transactional
    void getProductsByIdFiltering() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        Long id = product.getId();

        defaultProductShouldBeFound("id.equals=" + id);
        defaultProductShouldNotBeFound("id.notEquals=" + id);

        defaultProductShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultProductShouldNotBeFound("id.greaterThan=" + id);

        defaultProductShouldBeFound("id.lessThanOrEqual=" + id);
        defaultProductShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProductsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where description equals to DEFAULT_DESCRIPTION
        defaultProductShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the productList where description equals to UPDATED_DESCRIPTION
        defaultProductShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where description not equals to DEFAULT_DESCRIPTION
        defaultProductShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the productList where description not equals to UPDATED_DESCRIPTION
        defaultProductShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultProductShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the productList where description equals to UPDATED_DESCRIPTION
        defaultProductShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where description is not null
        defaultProductShouldBeFound("description.specified=true");

        // Get all the productList where description is null
        defaultProductShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where description contains DEFAULT_DESCRIPTION
        defaultProductShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the productList where description contains UPDATED_DESCRIPTION
        defaultProductShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where description does not contain DEFAULT_DESCRIPTION
        defaultProductShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the productList where description does not contain UPDATED_DESCRIPTION
        defaultProductShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllProductsByPhotoIdIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where photoId equals to DEFAULT_PHOTO_ID
        defaultProductShouldBeFound("photoId.equals=" + DEFAULT_PHOTO_ID);

        // Get all the productList where photoId equals to UPDATED_PHOTO_ID
        defaultProductShouldNotBeFound("photoId.equals=" + UPDATED_PHOTO_ID);
    }

    @Test
    @Transactional
    void getAllProductsByPhotoIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where photoId not equals to DEFAULT_PHOTO_ID
        defaultProductShouldNotBeFound("photoId.notEquals=" + DEFAULT_PHOTO_ID);

        // Get all the productList where photoId not equals to UPDATED_PHOTO_ID
        defaultProductShouldBeFound("photoId.notEquals=" + UPDATED_PHOTO_ID);
    }

    @Test
    @Transactional
    void getAllProductsByPhotoIdIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where photoId in DEFAULT_PHOTO_ID or UPDATED_PHOTO_ID
        defaultProductShouldBeFound("photoId.in=" + DEFAULT_PHOTO_ID + "," + UPDATED_PHOTO_ID);

        // Get all the productList where photoId equals to UPDATED_PHOTO_ID
        defaultProductShouldNotBeFound("photoId.in=" + UPDATED_PHOTO_ID);
    }

    @Test
    @Transactional
    void getAllProductsByPhotoIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where photoId is not null
        defaultProductShouldBeFound("photoId.specified=true");

        // Get all the productList where photoId is null
        defaultProductShouldNotBeFound("photoId.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByPhotoIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where photoId is greater than or equal to DEFAULT_PHOTO_ID
        defaultProductShouldBeFound("photoId.greaterThanOrEqual=" + DEFAULT_PHOTO_ID);

        // Get all the productList where photoId is greater than or equal to UPDATED_PHOTO_ID
        defaultProductShouldNotBeFound("photoId.greaterThanOrEqual=" + UPDATED_PHOTO_ID);
    }

    @Test
    @Transactional
    void getAllProductsByPhotoIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where photoId is less than or equal to DEFAULT_PHOTO_ID
        defaultProductShouldBeFound("photoId.lessThanOrEqual=" + DEFAULT_PHOTO_ID);

        // Get all the productList where photoId is less than or equal to SMALLER_PHOTO_ID
        defaultProductShouldNotBeFound("photoId.lessThanOrEqual=" + SMALLER_PHOTO_ID);
    }

    @Test
    @Transactional
    void getAllProductsByPhotoIdIsLessThanSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where photoId is less than DEFAULT_PHOTO_ID
        defaultProductShouldNotBeFound("photoId.lessThan=" + DEFAULT_PHOTO_ID);

        // Get all the productList where photoId is less than UPDATED_PHOTO_ID
        defaultProductShouldBeFound("photoId.lessThan=" + UPDATED_PHOTO_ID);
    }

    @Test
    @Transactional
    void getAllProductsByPhotoIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where photoId is greater than DEFAULT_PHOTO_ID
        defaultProductShouldNotBeFound("photoId.greaterThan=" + DEFAULT_PHOTO_ID);

        // Get all the productList where photoId is greater than SMALLER_PHOTO_ID
        defaultProductShouldBeFound("photoId.greaterThan=" + SMALLER_PHOTO_ID);
    }

    @Test
    @Transactional
    void getAllProductsByStockIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where stock equals to DEFAULT_STOCK
        defaultProductShouldBeFound("stock.equals=" + DEFAULT_STOCK);

        // Get all the productList where stock equals to UPDATED_STOCK
        defaultProductShouldNotBeFound("stock.equals=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    void getAllProductsByStockIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where stock not equals to DEFAULT_STOCK
        defaultProductShouldNotBeFound("stock.notEquals=" + DEFAULT_STOCK);

        // Get all the productList where stock not equals to UPDATED_STOCK
        defaultProductShouldBeFound("stock.notEquals=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    void getAllProductsByStockIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where stock in DEFAULT_STOCK or UPDATED_STOCK
        defaultProductShouldBeFound("stock.in=" + DEFAULT_STOCK + "," + UPDATED_STOCK);

        // Get all the productList where stock equals to UPDATED_STOCK
        defaultProductShouldNotBeFound("stock.in=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    void getAllProductsByStockIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where stock is not null
        defaultProductShouldBeFound("stock.specified=true");

        // Get all the productList where stock is null
        defaultProductShouldNotBeFound("stock.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByStockIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where stock is greater than or equal to DEFAULT_STOCK
        defaultProductShouldBeFound("stock.greaterThanOrEqual=" + DEFAULT_STOCK);

        // Get all the productList where stock is greater than or equal to UPDATED_STOCK
        defaultProductShouldNotBeFound("stock.greaterThanOrEqual=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    void getAllProductsByStockIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where stock is less than or equal to DEFAULT_STOCK
        defaultProductShouldBeFound("stock.lessThanOrEqual=" + DEFAULT_STOCK);

        // Get all the productList where stock is less than or equal to SMALLER_STOCK
        defaultProductShouldNotBeFound("stock.lessThanOrEqual=" + SMALLER_STOCK);
    }

    @Test
    @Transactional
    void getAllProductsByStockIsLessThanSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where stock is less than DEFAULT_STOCK
        defaultProductShouldNotBeFound("stock.lessThan=" + DEFAULT_STOCK);

        // Get all the productList where stock is less than UPDATED_STOCK
        defaultProductShouldBeFound("stock.lessThan=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    void getAllProductsByStockIsGreaterThanSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where stock is greater than DEFAULT_STOCK
        defaultProductShouldNotBeFound("stock.greaterThan=" + DEFAULT_STOCK);

        // Get all the productList where stock is greater than SMALLER_STOCK
        defaultProductShouldBeFound("stock.greaterThan=" + SMALLER_STOCK);
    }

    @Test
    @Transactional
    void getAllProductsByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where price equals to DEFAULT_PRICE
        defaultProductShouldBeFound("price.equals=" + DEFAULT_PRICE);

        // Get all the productList where price equals to UPDATED_PRICE
        defaultProductShouldNotBeFound("price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByPriceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where price not equals to DEFAULT_PRICE
        defaultProductShouldNotBeFound("price.notEquals=" + DEFAULT_PRICE);

        // Get all the productList where price not equals to UPDATED_PRICE
        defaultProductShouldBeFound("price.notEquals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where price in DEFAULT_PRICE or UPDATED_PRICE
        defaultProductShouldBeFound("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE);

        // Get all the productList where price equals to UPDATED_PRICE
        defaultProductShouldNotBeFound("price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where price is not null
        defaultProductShouldBeFound("price.specified=true");

        // Get all the productList where price is null
        defaultProductShouldNotBeFound("price.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where price is greater than or equal to DEFAULT_PRICE
        defaultProductShouldBeFound("price.greaterThanOrEqual=" + DEFAULT_PRICE);

        // Get all the productList where price is greater than or equal to UPDATED_PRICE
        defaultProductShouldNotBeFound("price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where price is less than or equal to DEFAULT_PRICE
        defaultProductShouldBeFound("price.lessThanOrEqual=" + DEFAULT_PRICE);

        // Get all the productList where price is less than or equal to SMALLER_PRICE
        defaultProductShouldNotBeFound("price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where price is less than DEFAULT_PRICE
        defaultProductShouldNotBeFound("price.lessThan=" + DEFAULT_PRICE);

        // Get all the productList where price is less than UPDATED_PRICE
        defaultProductShouldBeFound("price.lessThan=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where price is greater than DEFAULT_PRICE
        defaultProductShouldNotBeFound("price.greaterThan=" + DEFAULT_PRICE);

        // Get all the productList where price is greater than SMALLER_PRICE
        defaultProductShouldBeFound("price.greaterThan=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllProductsByModelNameIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where modelName equals to DEFAULT_MODEL_NAME
        defaultProductShouldBeFound("modelName.equals=" + DEFAULT_MODEL_NAME);

        // Get all the productList where modelName equals to UPDATED_MODEL_NAME
        defaultProductShouldNotBeFound("modelName.equals=" + UPDATED_MODEL_NAME);
    }

    @Test
    @Transactional
    void getAllProductsByModelNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where modelName not equals to DEFAULT_MODEL_NAME
        defaultProductShouldNotBeFound("modelName.notEquals=" + DEFAULT_MODEL_NAME);

        // Get all the productList where modelName not equals to UPDATED_MODEL_NAME
        defaultProductShouldBeFound("modelName.notEquals=" + UPDATED_MODEL_NAME);
    }

    @Test
    @Transactional
    void getAllProductsByModelNameIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where modelName in DEFAULT_MODEL_NAME or UPDATED_MODEL_NAME
        defaultProductShouldBeFound("modelName.in=" + DEFAULT_MODEL_NAME + "," + UPDATED_MODEL_NAME);

        // Get all the productList where modelName equals to UPDATED_MODEL_NAME
        defaultProductShouldNotBeFound("modelName.in=" + UPDATED_MODEL_NAME);
    }

    @Test
    @Transactional
    void getAllProductsByModelNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where modelName is not null
        defaultProductShouldBeFound("modelName.specified=true");

        // Get all the productList where modelName is null
        defaultProductShouldNotBeFound("modelName.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsByModelNameContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where modelName contains DEFAULT_MODEL_NAME
        defaultProductShouldBeFound("modelName.contains=" + DEFAULT_MODEL_NAME);

        // Get all the productList where modelName contains UPDATED_MODEL_NAME
        defaultProductShouldNotBeFound("modelName.contains=" + UPDATED_MODEL_NAME);
    }

    @Test
    @Transactional
    void getAllProductsByModelNameNotContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where modelName does not contain DEFAULT_MODEL_NAME
        defaultProductShouldNotBeFound("modelName.doesNotContain=" + DEFAULT_MODEL_NAME);

        // Get all the productList where modelName does not contain UPDATED_MODEL_NAME
        defaultProductShouldBeFound("modelName.doesNotContain=" + UPDATED_MODEL_NAME);
    }

    @Test
    @Transactional
    void getAllProductsByColorIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where color equals to DEFAULT_COLOR
        defaultProductShouldBeFound("color.equals=" + DEFAULT_COLOR);

        // Get all the productList where color equals to UPDATED_COLOR
        defaultProductShouldNotBeFound("color.equals=" + UPDATED_COLOR);
    }

    @Test
    @Transactional
    void getAllProductsByColorIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where color not equals to DEFAULT_COLOR
        defaultProductShouldNotBeFound("color.notEquals=" + DEFAULT_COLOR);

        // Get all the productList where color not equals to UPDATED_COLOR
        defaultProductShouldBeFound("color.notEquals=" + UPDATED_COLOR);
    }

    @Test
    @Transactional
    void getAllProductsByColorIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where color in DEFAULT_COLOR or UPDATED_COLOR
        defaultProductShouldBeFound("color.in=" + DEFAULT_COLOR + "," + UPDATED_COLOR);

        // Get all the productList where color equals to UPDATED_COLOR
        defaultProductShouldNotBeFound("color.in=" + UPDATED_COLOR);
    }

    @Test
    @Transactional
    void getAllProductsByColorIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where color is not null
        defaultProductShouldBeFound("color.specified=true");

        // Get all the productList where color is null
        defaultProductShouldNotBeFound("color.specified=false");
    }

    @Test
    @Transactional
    void getAllProductsBySubFamilyIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);
        SubFamily subFamily;
        if (TestUtil.findAll(em, SubFamily.class).isEmpty()) {
            subFamily = SubFamilyResourceIT.createEntity(em);
            em.persist(subFamily);
            em.flush();
        } else {
            subFamily = TestUtil.findAll(em, SubFamily.class).get(0);
        }
        em.persist(subFamily);
        em.flush();
        product.setSubFamily(subFamily);
        productRepository.saveAndFlush(product);
        Long subFamilyId = subFamily.getId();

        // Get all the productList where subFamily equals to subFamilyId
        defaultProductShouldBeFound("subFamilyId.equals=" + subFamilyId);

        // Get all the productList where subFamily equals to (subFamilyId + 1)
        defaultProductShouldNotBeFound("subFamilyId.equals=" + (subFamilyId + 1));
    }

    @Test
    @Transactional
    void getAllProductsByBrandIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);
        Brand brand;
        if (TestUtil.findAll(em, Brand.class).isEmpty()) {
            brand = BrandResourceIT.createEntity(em);
            em.persist(brand);
            em.flush();
        } else {
            brand = TestUtil.findAll(em, Brand.class).get(0);
        }
        em.persist(brand);
        em.flush();
        product.setBrand(brand);
        productRepository.saveAndFlush(product);
        Long brandId = brand.getId();

        // Get all the productList where brand equals to brandId
        defaultProductShouldBeFound("brandId.equals=" + brandId);

        // Get all the productList where brand equals to (brandId + 1)
        defaultProductShouldNotBeFound("brandId.equals=" + (brandId + 1));
    }

    @Test
    @Transactional
    void getAllProductsBySellerIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);
        Seller seller;
        if (TestUtil.findAll(em, Seller.class).isEmpty()) {
            seller = SellerResourceIT.createEntity(em);
            em.persist(seller);
            em.flush();
        } else {
            seller = TestUtil.findAll(em, Seller.class).get(0);
        }
        em.persist(seller);
        em.flush();
        product.setSeller(seller);
        productRepository.saveAndFlush(product);
        Long sellerId = seller.getId();

        // Get all the productList where seller equals to sellerId
        defaultProductShouldBeFound("sellerId.equals=" + sellerId);

        // Get all the productList where seller equals to (sellerId + 1)
        defaultProductShouldNotBeFound("sellerId.equals=" + (sellerId + 1));
    }

    @Test
    @Transactional
    void getAllProductsByTagsIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);
        Tag tags;
        if (TestUtil.findAll(em, Tag.class).isEmpty()) {
            tags = TagResourceIT.createEntity(em);
            em.persist(tags);
            em.flush();
        } else {
            tags = TestUtil.findAll(em, Tag.class).get(0);
        }
        em.persist(tags);
        em.flush();
        product.addTags(tags);
        productRepository.saveAndFlush(product);
        Long tagsId = tags.getId();

        // Get all the productList where tags equals to tagsId
        defaultProductShouldBeFound("tagsId.equals=" + tagsId);

        // Get all the productList where tags equals to (tagsId + 1)
        defaultProductShouldNotBeFound("tagsId.equals=" + (tagsId + 1));
    }

    @Test
    @Transactional
    void getAllProductsByOrderIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);
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
        product.addOrder(order);
        productRepository.saveAndFlush(product);
        Long orderId = order.getId();

        // Get all the productList where order equals to orderId
        defaultProductShouldBeFound("orderId.equals=" + orderId);

        // Get all the productList where order equals to (orderId + 1)
        defaultProductShouldNotBeFound("orderId.equals=" + (orderId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductShouldBeFound(String filter) throws Exception {
        restProductMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(product.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].photoId").value(hasItem(DEFAULT_PHOTO_ID.intValue())))
            .andExpect(jsonPath("$.[*].stock").value(hasItem(DEFAULT_STOCK.intValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].modelName").value(hasItem(DEFAULT_MODEL_NAME)))
            .andExpect(jsonPath("$.[*].color").value(hasItem(DEFAULT_COLOR.toString())));

        // Check, that the count call also returns 1
        restProductMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductShouldNotBeFound(String filter) throws Exception {
        restProductMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProduct() throws Exception {
        // Get the product
        restProductMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewProduct() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        int databaseSizeBeforeUpdate = productRepository.findAll().size();

        // Update the product
        Product updatedProduct = productRepository.findById(product.getId()).get();
        // Disconnect from session so that the updates on updatedProduct are not directly saved in db
        em.detach(updatedProduct);
        updatedProduct
            .description(UPDATED_DESCRIPTION)
            .photoId(UPDATED_PHOTO_ID)
            .stock(UPDATED_STOCK)
            .price(UPDATED_PRICE)
            .modelName(UPDATED_MODEL_NAME)
            .color(UPDATED_COLOR);

        restProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedProduct.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedProduct))
            )
            .andExpect(status().isOk());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
        Product testProduct = productList.get(productList.size() - 1);
        assertThat(testProduct.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProduct.getPhotoId()).isEqualTo(UPDATED_PHOTO_ID);
        assertThat(testProduct.getStock()).isEqualTo(UPDATED_STOCK);
        assertThat(testProduct.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testProduct.getModelName()).isEqualTo(UPDATED_MODEL_NAME);
        assertThat(testProduct.getColor()).isEqualTo(UPDATED_COLOR);
    }

    @Test
    @Transactional
    void putNonExistingProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().size();
        product.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, product.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(product))
            )
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().size();
        product.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(product))
            )
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().size();
        product.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(product)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductWithPatch() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        int databaseSizeBeforeUpdate = productRepository.findAll().size();

        // Update the product using partial update
        Product partialUpdatedProduct = new Product();
        partialUpdatedProduct.setId(product.getId());

        partialUpdatedProduct.modelName(UPDATED_MODEL_NAME).color(UPDATED_COLOR);

        restProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProduct.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProduct))
            )
            .andExpect(status().isOk());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
        Product testProduct = productList.get(productList.size() - 1);
        assertThat(testProduct.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProduct.getPhotoId()).isEqualTo(DEFAULT_PHOTO_ID);
        assertThat(testProduct.getStock()).isEqualTo(DEFAULT_STOCK);
        assertThat(testProduct.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testProduct.getModelName()).isEqualTo(UPDATED_MODEL_NAME);
        assertThat(testProduct.getColor()).isEqualTo(UPDATED_COLOR);
    }

    @Test
    @Transactional
    void fullUpdateProductWithPatch() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        int databaseSizeBeforeUpdate = productRepository.findAll().size();

        // Update the product using partial update
        Product partialUpdatedProduct = new Product();
        partialUpdatedProduct.setId(product.getId());

        partialUpdatedProduct
            .description(UPDATED_DESCRIPTION)
            .photoId(UPDATED_PHOTO_ID)
            .stock(UPDATED_STOCK)
            .price(UPDATED_PRICE)
            .modelName(UPDATED_MODEL_NAME)
            .color(UPDATED_COLOR);

        restProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProduct.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProduct))
            )
            .andExpect(status().isOk());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
        Product testProduct = productList.get(productList.size() - 1);
        assertThat(testProduct.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProduct.getPhotoId()).isEqualTo(UPDATED_PHOTO_ID);
        assertThat(testProduct.getStock()).isEqualTo(UPDATED_STOCK);
        assertThat(testProduct.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testProduct.getModelName()).isEqualTo(UPDATED_MODEL_NAME);
        assertThat(testProduct.getColor()).isEqualTo(UPDATED_COLOR);
    }

    @Test
    @Transactional
    void patchNonExistingProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().size();
        product.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, product.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(product))
            )
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().size();
        product.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(product))
            )
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().size();
        product.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(product)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProduct() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        int databaseSizeBeforeDelete = productRepository.findAll().size();

        // Delete the product
        restProductMockMvc
            .perform(delete(ENTITY_API_URL_ID, product.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
