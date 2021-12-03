package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Client;
import com.mycompany.myapp.domain.Order;
import com.mycompany.myapp.domain.Product;
import com.mycompany.myapp.domain.enumeration.BillingMethod;
import com.mycompany.myapp.domain.enumeration.OrderState;
import com.mycompany.myapp.repository.OrderRepository;
import com.mycompany.myapp.service.OrderService;
import com.mycompany.myapp.service.criteria.OrderCriteria;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
 * Integration tests for the {@link OrderResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class OrderResourceIT {

    private static final Double DEFAULT_SUM = 1D;
    private static final Double UPDATED_SUM = 2D;
    private static final Double SMALLER_SUM = 1D - 1D;

    private static final String DEFAULT_DELIVERY_ADRESS = "AAAAAAAAAA";
    private static final String UPDATED_DELIVERY_ADRESS = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DELIVERY_DATE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DELIVERY_DATE_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_DELIVERY_DATE_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final Long DEFAULT_QUANTITY = 1L;
    private static final Long UPDATED_QUANTITY = 2L;
    private static final Long SMALLER_QUANTITY = 1L - 1L;

    private static final BillingMethod DEFAULT_BILLING_METHOD = BillingMethod.PAYPAL;
    private static final BillingMethod UPDATED_BILLING_METHOD = BillingMethod.CREDIT_CARD;

    private static final OrderState DEFAULT_ORDER_STATE = OrderState.PROCESSING;
    private static final OrderState UPDATED_ORDER_STATE = OrderState.PAID;

    private static final String ENTITY_API_URL = "/api/orders";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OrderRepository orderRepository;

    @Mock
    private OrderRepository orderRepositoryMock;

    @Mock
    private OrderService orderServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrderMockMvc;

    private Order order;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Order createEntity(EntityManager em) {
        Order order = new Order()
            .sum(DEFAULT_SUM)
            .deliveryAdress(DEFAULT_DELIVERY_ADRESS)
            .deliveryDateTime(DEFAULT_DELIVERY_DATE_TIME)
            .quantity(DEFAULT_QUANTITY)
            .billingMethod(DEFAULT_BILLING_METHOD)
            .orderState(DEFAULT_ORDER_STATE);
        return order;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Order createUpdatedEntity(EntityManager em) {
        Order order = new Order()
            .sum(UPDATED_SUM)
            .deliveryAdress(UPDATED_DELIVERY_ADRESS)
            .deliveryDateTime(UPDATED_DELIVERY_DATE_TIME)
            .quantity(UPDATED_QUANTITY)
            .billingMethod(UPDATED_BILLING_METHOD)
            .orderState(UPDATED_ORDER_STATE);
        return order;
    }

    @BeforeEach
    public void initTest() {
        order = createEntity(em);
    }

    @Test
    @Transactional
    void createOrder() throws Exception {
        int databaseSizeBeforeCreate = orderRepository.findAll().size();
        // Create the Order
        restOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(order)))
            .andExpect(status().isCreated());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeCreate + 1);
        Order testOrder = orderList.get(orderList.size() - 1);
        assertThat(testOrder.getSum()).isEqualTo(DEFAULT_SUM);
        assertThat(testOrder.getDeliveryAdress()).isEqualTo(DEFAULT_DELIVERY_ADRESS);
        assertThat(testOrder.getDeliveryDateTime()).isEqualTo(DEFAULT_DELIVERY_DATE_TIME);
        assertThat(testOrder.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testOrder.getBillingMethod()).isEqualTo(DEFAULT_BILLING_METHOD);
        assertThat(testOrder.getOrderState()).isEqualTo(DEFAULT_ORDER_STATE);
    }

    @Test
    @Transactional
    void createOrderWithExistingId() throws Exception {
        // Create the Order with an existing ID
        order.setId(1L);

        int databaseSizeBeforeCreate = orderRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(order)))
            .andExpect(status().isBadRequest());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDeliveryAdressIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderRepository.findAll().size();
        // set the field null
        order.setDeliveryAdress(null);

        // Create the Order, which fails.

        restOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(order)))
            .andExpect(status().isBadRequest());

        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDeliveryDateTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderRepository.findAll().size();
        // set the field null
        order.setDeliveryDateTime(null);

        // Create the Order, which fails.

        restOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(order)))
            .andExpect(status().isBadRequest());

        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderRepository.findAll().size();
        // set the field null
        order.setQuantity(null);

        // Create the Order, which fails.

        restOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(order)))
            .andExpect(status().isBadRequest());

        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBillingMethodIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderRepository.findAll().size();
        // set the field null
        order.setBillingMethod(null);

        // Create the Order, which fails.

        restOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(order)))
            .andExpect(status().isBadRequest());

        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOrderStateIsRequired() throws Exception {
        int databaseSizeBeforeTest = orderRepository.findAll().size();
        // set the field null
        order.setOrderState(null);

        // Create the Order, which fails.

        restOrderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(order)))
            .andExpect(status().isBadRequest());

        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOrders() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList
        restOrderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(order.getId().intValue())))
            .andExpect(jsonPath("$.[*].sum").value(hasItem(DEFAULT_SUM.doubleValue())))
            .andExpect(jsonPath("$.[*].deliveryAdress").value(hasItem(DEFAULT_DELIVERY_ADRESS)))
            .andExpect(jsonPath("$.[*].deliveryDateTime").value(hasItem(sameInstant(DEFAULT_DELIVERY_DATE_TIME))))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY.intValue())))
            .andExpect(jsonPath("$.[*].billingMethod").value(hasItem(DEFAULT_BILLING_METHOD.toString())))
            .andExpect(jsonPath("$.[*].orderState").value(hasItem(DEFAULT_ORDER_STATE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOrdersWithEagerRelationshipsIsEnabled() throws Exception {
        when(orderServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOrderMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(orderServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOrdersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(orderServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOrderMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(orderServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getOrder() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get the order
        restOrderMockMvc
            .perform(get(ENTITY_API_URL_ID, order.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(order.getId().intValue()))
            .andExpect(jsonPath("$.sum").value(DEFAULT_SUM.doubleValue()))
            .andExpect(jsonPath("$.deliveryAdress").value(DEFAULT_DELIVERY_ADRESS))
            .andExpect(jsonPath("$.deliveryDateTime").value(sameInstant(DEFAULT_DELIVERY_DATE_TIME)))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY.intValue()))
            .andExpect(jsonPath("$.billingMethod").value(DEFAULT_BILLING_METHOD.toString()))
            .andExpect(jsonPath("$.orderState").value(DEFAULT_ORDER_STATE.toString()));
    }

    @Test
    @Transactional
    void getOrdersByIdFiltering() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        Long id = order.getId();

        defaultOrderShouldBeFound("id.equals=" + id);
        defaultOrderShouldNotBeFound("id.notEquals=" + id);

        defaultOrderShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultOrderShouldNotBeFound("id.greaterThan=" + id);

        defaultOrderShouldBeFound("id.lessThanOrEqual=" + id);
        defaultOrderShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOrdersBySumIsEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where sum equals to DEFAULT_SUM
        defaultOrderShouldBeFound("sum.equals=" + DEFAULT_SUM);

        // Get all the orderList where sum equals to UPDATED_SUM
        defaultOrderShouldNotBeFound("sum.equals=" + UPDATED_SUM);
    }

    @Test
    @Transactional
    void getAllOrdersBySumIsNotEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where sum not equals to DEFAULT_SUM
        defaultOrderShouldNotBeFound("sum.notEquals=" + DEFAULT_SUM);

        // Get all the orderList where sum not equals to UPDATED_SUM
        defaultOrderShouldBeFound("sum.notEquals=" + UPDATED_SUM);
    }

    @Test
    @Transactional
    void getAllOrdersBySumIsInShouldWork() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where sum in DEFAULT_SUM or UPDATED_SUM
        defaultOrderShouldBeFound("sum.in=" + DEFAULT_SUM + "," + UPDATED_SUM);

        // Get all the orderList where sum equals to UPDATED_SUM
        defaultOrderShouldNotBeFound("sum.in=" + UPDATED_SUM);
    }

    @Test
    @Transactional
    void getAllOrdersBySumIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where sum is not null
        defaultOrderShouldBeFound("sum.specified=true");

        // Get all the orderList where sum is null
        defaultOrderShouldNotBeFound("sum.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdersBySumIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where sum is greater than or equal to DEFAULT_SUM
        defaultOrderShouldBeFound("sum.greaterThanOrEqual=" + DEFAULT_SUM);

        // Get all the orderList where sum is greater than or equal to UPDATED_SUM
        defaultOrderShouldNotBeFound("sum.greaterThanOrEqual=" + UPDATED_SUM);
    }

    @Test
    @Transactional
    void getAllOrdersBySumIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where sum is less than or equal to DEFAULT_SUM
        defaultOrderShouldBeFound("sum.lessThanOrEqual=" + DEFAULT_SUM);

        // Get all the orderList where sum is less than or equal to SMALLER_SUM
        defaultOrderShouldNotBeFound("sum.lessThanOrEqual=" + SMALLER_SUM);
    }

    @Test
    @Transactional
    void getAllOrdersBySumIsLessThanSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where sum is less than DEFAULT_SUM
        defaultOrderShouldNotBeFound("sum.lessThan=" + DEFAULT_SUM);

        // Get all the orderList where sum is less than UPDATED_SUM
        defaultOrderShouldBeFound("sum.lessThan=" + UPDATED_SUM);
    }

    @Test
    @Transactional
    void getAllOrdersBySumIsGreaterThanSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where sum is greater than DEFAULT_SUM
        defaultOrderShouldNotBeFound("sum.greaterThan=" + DEFAULT_SUM);

        // Get all the orderList where sum is greater than SMALLER_SUM
        defaultOrderShouldBeFound("sum.greaterThan=" + SMALLER_SUM);
    }

    @Test
    @Transactional
    void getAllOrdersByDeliveryAdressIsEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where deliveryAdress equals to DEFAULT_DELIVERY_ADRESS
        defaultOrderShouldBeFound("deliveryAdress.equals=" + DEFAULT_DELIVERY_ADRESS);

        // Get all the orderList where deliveryAdress equals to UPDATED_DELIVERY_ADRESS
        defaultOrderShouldNotBeFound("deliveryAdress.equals=" + UPDATED_DELIVERY_ADRESS);
    }

    @Test
    @Transactional
    void getAllOrdersByDeliveryAdressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where deliveryAdress not equals to DEFAULT_DELIVERY_ADRESS
        defaultOrderShouldNotBeFound("deliveryAdress.notEquals=" + DEFAULT_DELIVERY_ADRESS);

        // Get all the orderList where deliveryAdress not equals to UPDATED_DELIVERY_ADRESS
        defaultOrderShouldBeFound("deliveryAdress.notEquals=" + UPDATED_DELIVERY_ADRESS);
    }

    @Test
    @Transactional
    void getAllOrdersByDeliveryAdressIsInShouldWork() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where deliveryAdress in DEFAULT_DELIVERY_ADRESS or UPDATED_DELIVERY_ADRESS
        defaultOrderShouldBeFound("deliveryAdress.in=" + DEFAULT_DELIVERY_ADRESS + "," + UPDATED_DELIVERY_ADRESS);

        // Get all the orderList where deliveryAdress equals to UPDATED_DELIVERY_ADRESS
        defaultOrderShouldNotBeFound("deliveryAdress.in=" + UPDATED_DELIVERY_ADRESS);
    }

    @Test
    @Transactional
    void getAllOrdersByDeliveryAdressIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where deliveryAdress is not null
        defaultOrderShouldBeFound("deliveryAdress.specified=true");

        // Get all the orderList where deliveryAdress is null
        defaultOrderShouldNotBeFound("deliveryAdress.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdersByDeliveryAdressContainsSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where deliveryAdress contains DEFAULT_DELIVERY_ADRESS
        defaultOrderShouldBeFound("deliveryAdress.contains=" + DEFAULT_DELIVERY_ADRESS);

        // Get all the orderList where deliveryAdress contains UPDATED_DELIVERY_ADRESS
        defaultOrderShouldNotBeFound("deliveryAdress.contains=" + UPDATED_DELIVERY_ADRESS);
    }

    @Test
    @Transactional
    void getAllOrdersByDeliveryAdressNotContainsSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where deliveryAdress does not contain DEFAULT_DELIVERY_ADRESS
        defaultOrderShouldNotBeFound("deliveryAdress.doesNotContain=" + DEFAULT_DELIVERY_ADRESS);

        // Get all the orderList where deliveryAdress does not contain UPDATED_DELIVERY_ADRESS
        defaultOrderShouldBeFound("deliveryAdress.doesNotContain=" + UPDATED_DELIVERY_ADRESS);
    }

    @Test
    @Transactional
    void getAllOrdersByDeliveryDateTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where deliveryDateTime equals to DEFAULT_DELIVERY_DATE_TIME
        defaultOrderShouldBeFound("deliveryDateTime.equals=" + DEFAULT_DELIVERY_DATE_TIME);

        // Get all the orderList where deliveryDateTime equals to UPDATED_DELIVERY_DATE_TIME
        defaultOrderShouldNotBeFound("deliveryDateTime.equals=" + UPDATED_DELIVERY_DATE_TIME);
    }

    @Test
    @Transactional
    void getAllOrdersByDeliveryDateTimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where deliveryDateTime not equals to DEFAULT_DELIVERY_DATE_TIME
        defaultOrderShouldNotBeFound("deliveryDateTime.notEquals=" + DEFAULT_DELIVERY_DATE_TIME);

        // Get all the orderList where deliveryDateTime not equals to UPDATED_DELIVERY_DATE_TIME
        defaultOrderShouldBeFound("deliveryDateTime.notEquals=" + UPDATED_DELIVERY_DATE_TIME);
    }

    @Test
    @Transactional
    void getAllOrdersByDeliveryDateTimeIsInShouldWork() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where deliveryDateTime in DEFAULT_DELIVERY_DATE_TIME or UPDATED_DELIVERY_DATE_TIME
        defaultOrderShouldBeFound("deliveryDateTime.in=" + DEFAULT_DELIVERY_DATE_TIME + "," + UPDATED_DELIVERY_DATE_TIME);

        // Get all the orderList where deliveryDateTime equals to UPDATED_DELIVERY_DATE_TIME
        defaultOrderShouldNotBeFound("deliveryDateTime.in=" + UPDATED_DELIVERY_DATE_TIME);
    }

    @Test
    @Transactional
    void getAllOrdersByDeliveryDateTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where deliveryDateTime is not null
        defaultOrderShouldBeFound("deliveryDateTime.specified=true");

        // Get all the orderList where deliveryDateTime is null
        defaultOrderShouldNotBeFound("deliveryDateTime.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdersByDeliveryDateTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where deliveryDateTime is greater than or equal to DEFAULT_DELIVERY_DATE_TIME
        defaultOrderShouldBeFound("deliveryDateTime.greaterThanOrEqual=" + DEFAULT_DELIVERY_DATE_TIME);

        // Get all the orderList where deliveryDateTime is greater than or equal to UPDATED_DELIVERY_DATE_TIME
        defaultOrderShouldNotBeFound("deliveryDateTime.greaterThanOrEqual=" + UPDATED_DELIVERY_DATE_TIME);
    }

    @Test
    @Transactional
    void getAllOrdersByDeliveryDateTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where deliveryDateTime is less than or equal to DEFAULT_DELIVERY_DATE_TIME
        defaultOrderShouldBeFound("deliveryDateTime.lessThanOrEqual=" + DEFAULT_DELIVERY_DATE_TIME);

        // Get all the orderList where deliveryDateTime is less than or equal to SMALLER_DELIVERY_DATE_TIME
        defaultOrderShouldNotBeFound("deliveryDateTime.lessThanOrEqual=" + SMALLER_DELIVERY_DATE_TIME);
    }

    @Test
    @Transactional
    void getAllOrdersByDeliveryDateTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where deliveryDateTime is less than DEFAULT_DELIVERY_DATE_TIME
        defaultOrderShouldNotBeFound("deliveryDateTime.lessThan=" + DEFAULT_DELIVERY_DATE_TIME);

        // Get all the orderList where deliveryDateTime is less than UPDATED_DELIVERY_DATE_TIME
        defaultOrderShouldBeFound("deliveryDateTime.lessThan=" + UPDATED_DELIVERY_DATE_TIME);
    }

    @Test
    @Transactional
    void getAllOrdersByDeliveryDateTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where deliveryDateTime is greater than DEFAULT_DELIVERY_DATE_TIME
        defaultOrderShouldNotBeFound("deliveryDateTime.greaterThan=" + DEFAULT_DELIVERY_DATE_TIME);

        // Get all the orderList where deliveryDateTime is greater than SMALLER_DELIVERY_DATE_TIME
        defaultOrderShouldBeFound("deliveryDateTime.greaterThan=" + SMALLER_DELIVERY_DATE_TIME);
    }

    @Test
    @Transactional
    void getAllOrdersByQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where quantity equals to DEFAULT_QUANTITY
        defaultOrderShouldBeFound("quantity.equals=" + DEFAULT_QUANTITY);

        // Get all the orderList where quantity equals to UPDATED_QUANTITY
        defaultOrderShouldNotBeFound("quantity.equals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllOrdersByQuantityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where quantity not equals to DEFAULT_QUANTITY
        defaultOrderShouldNotBeFound("quantity.notEquals=" + DEFAULT_QUANTITY);

        // Get all the orderList where quantity not equals to UPDATED_QUANTITY
        defaultOrderShouldBeFound("quantity.notEquals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllOrdersByQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where quantity in DEFAULT_QUANTITY or UPDATED_QUANTITY
        defaultOrderShouldBeFound("quantity.in=" + DEFAULT_QUANTITY + "," + UPDATED_QUANTITY);

        // Get all the orderList where quantity equals to UPDATED_QUANTITY
        defaultOrderShouldNotBeFound("quantity.in=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllOrdersByQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where quantity is not null
        defaultOrderShouldBeFound("quantity.specified=true");

        // Get all the orderList where quantity is null
        defaultOrderShouldNotBeFound("quantity.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdersByQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where quantity is greater than or equal to DEFAULT_QUANTITY
        defaultOrderShouldBeFound("quantity.greaterThanOrEqual=" + DEFAULT_QUANTITY);

        // Get all the orderList where quantity is greater than or equal to UPDATED_QUANTITY
        defaultOrderShouldNotBeFound("quantity.greaterThanOrEqual=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllOrdersByQuantityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where quantity is less than or equal to DEFAULT_QUANTITY
        defaultOrderShouldBeFound("quantity.lessThanOrEqual=" + DEFAULT_QUANTITY);

        // Get all the orderList where quantity is less than or equal to SMALLER_QUANTITY
        defaultOrderShouldNotBeFound("quantity.lessThanOrEqual=" + SMALLER_QUANTITY);
    }

    @Test
    @Transactional
    void getAllOrdersByQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where quantity is less than DEFAULT_QUANTITY
        defaultOrderShouldNotBeFound("quantity.lessThan=" + DEFAULT_QUANTITY);

        // Get all the orderList where quantity is less than UPDATED_QUANTITY
        defaultOrderShouldBeFound("quantity.lessThan=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllOrdersByQuantityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where quantity is greater than DEFAULT_QUANTITY
        defaultOrderShouldNotBeFound("quantity.greaterThan=" + DEFAULT_QUANTITY);

        // Get all the orderList where quantity is greater than SMALLER_QUANTITY
        defaultOrderShouldBeFound("quantity.greaterThan=" + SMALLER_QUANTITY);
    }

    @Test
    @Transactional
    void getAllOrdersByBillingMethodIsEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where billingMethod equals to DEFAULT_BILLING_METHOD
        defaultOrderShouldBeFound("billingMethod.equals=" + DEFAULT_BILLING_METHOD);

        // Get all the orderList where billingMethod equals to UPDATED_BILLING_METHOD
        defaultOrderShouldNotBeFound("billingMethod.equals=" + UPDATED_BILLING_METHOD);
    }

    @Test
    @Transactional
    void getAllOrdersByBillingMethodIsNotEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where billingMethod not equals to DEFAULT_BILLING_METHOD
        defaultOrderShouldNotBeFound("billingMethod.notEquals=" + DEFAULT_BILLING_METHOD);

        // Get all the orderList where billingMethod not equals to UPDATED_BILLING_METHOD
        defaultOrderShouldBeFound("billingMethod.notEquals=" + UPDATED_BILLING_METHOD);
    }

    @Test
    @Transactional
    void getAllOrdersByBillingMethodIsInShouldWork() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where billingMethod in DEFAULT_BILLING_METHOD or UPDATED_BILLING_METHOD
        defaultOrderShouldBeFound("billingMethod.in=" + DEFAULT_BILLING_METHOD + "," + UPDATED_BILLING_METHOD);

        // Get all the orderList where billingMethod equals to UPDATED_BILLING_METHOD
        defaultOrderShouldNotBeFound("billingMethod.in=" + UPDATED_BILLING_METHOD);
    }

    @Test
    @Transactional
    void getAllOrdersByBillingMethodIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where billingMethod is not null
        defaultOrderShouldBeFound("billingMethod.specified=true");

        // Get all the orderList where billingMethod is null
        defaultOrderShouldNotBeFound("billingMethod.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdersByOrderStateIsEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where orderState equals to DEFAULT_ORDER_STATE
        defaultOrderShouldBeFound("orderState.equals=" + DEFAULT_ORDER_STATE);

        // Get all the orderList where orderState equals to UPDATED_ORDER_STATE
        defaultOrderShouldNotBeFound("orderState.equals=" + UPDATED_ORDER_STATE);
    }

    @Test
    @Transactional
    void getAllOrdersByOrderStateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where orderState not equals to DEFAULT_ORDER_STATE
        defaultOrderShouldNotBeFound("orderState.notEquals=" + DEFAULT_ORDER_STATE);

        // Get all the orderList where orderState not equals to UPDATED_ORDER_STATE
        defaultOrderShouldBeFound("orderState.notEquals=" + UPDATED_ORDER_STATE);
    }

    @Test
    @Transactional
    void getAllOrdersByOrderStateIsInShouldWork() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where orderState in DEFAULT_ORDER_STATE or UPDATED_ORDER_STATE
        defaultOrderShouldBeFound("orderState.in=" + DEFAULT_ORDER_STATE + "," + UPDATED_ORDER_STATE);

        // Get all the orderList where orderState equals to UPDATED_ORDER_STATE
        defaultOrderShouldNotBeFound("orderState.in=" + UPDATED_ORDER_STATE);
    }

    @Test
    @Transactional
    void getAllOrdersByOrderStateIsNullOrNotNull() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        // Get all the orderList where orderState is not null
        defaultOrderShouldBeFound("orderState.specified=true");

        // Get all the orderList where orderState is null
        defaultOrderShouldNotBeFound("orderState.specified=false");
    }

    @Test
    @Transactional
    void getAllOrdersByProductsIsEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);
        Product products;
        if (TestUtil.findAll(em, Product.class).isEmpty()) {
            products = ProductResourceIT.createEntity(em);
            em.persist(products);
            em.flush();
        } else {
            products = TestUtil.findAll(em, Product.class).get(0);
        }
        em.persist(products);
        em.flush();
        order.addProducts(products);
        orderRepository.saveAndFlush(order);
        Long productsId = products.getId();

        // Get all the orderList where products equals to productsId
        defaultOrderShouldBeFound("productsId.equals=" + productsId);

        // Get all the orderList where products equals to (productsId + 1)
        defaultOrderShouldNotBeFound("productsId.equals=" + (productsId + 1));
    }

    @Test
    @Transactional
    void getAllOrdersByClientIsEqualToSomething() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);
        Client client;
        if (TestUtil.findAll(em, Client.class).isEmpty()) {
            client = ClientResourceIT.createEntity(em);
            em.persist(client);
            em.flush();
        } else {
            client = TestUtil.findAll(em, Client.class).get(0);
        }
        em.persist(client);
        em.flush();
        order.setClient(client);
        orderRepository.saveAndFlush(order);
        Long clientId = client.getId();

        // Get all the orderList where client equals to clientId
        defaultOrderShouldBeFound("clientId.equals=" + clientId);

        // Get all the orderList where client equals to (clientId + 1)
        defaultOrderShouldNotBeFound("clientId.equals=" + (clientId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOrderShouldBeFound(String filter) throws Exception {
        restOrderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(order.getId().intValue())))
            .andExpect(jsonPath("$.[*].sum").value(hasItem(DEFAULT_SUM.doubleValue())))
            .andExpect(jsonPath("$.[*].deliveryAdress").value(hasItem(DEFAULT_DELIVERY_ADRESS)))
            .andExpect(jsonPath("$.[*].deliveryDateTime").value(hasItem(sameInstant(DEFAULT_DELIVERY_DATE_TIME))))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY.intValue())))
            .andExpect(jsonPath("$.[*].billingMethod").value(hasItem(DEFAULT_BILLING_METHOD.toString())))
            .andExpect(jsonPath("$.[*].orderState").value(hasItem(DEFAULT_ORDER_STATE.toString())));

        // Check, that the count call also returns 1
        restOrderMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOrderShouldNotBeFound(String filter) throws Exception {
        restOrderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOrderMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOrder() throws Exception {
        // Get the order
        restOrderMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOrder() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        int databaseSizeBeforeUpdate = orderRepository.findAll().size();

        // Update the order
        Order updatedOrder = orderRepository.findById(order.getId()).get();
        // Disconnect from session so that the updates on updatedOrder are not directly saved in db
        em.detach(updatedOrder);
        updatedOrder
            .sum(UPDATED_SUM)
            .deliveryAdress(UPDATED_DELIVERY_ADRESS)
            .deliveryDateTime(UPDATED_DELIVERY_DATE_TIME)
            .quantity(UPDATED_QUANTITY)
            .billingMethod(UPDATED_BILLING_METHOD)
            .orderState(UPDATED_ORDER_STATE);

        restOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOrder.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOrder))
            )
            .andExpect(status().isOk());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
        Order testOrder = orderList.get(orderList.size() - 1);
        assertThat(testOrder.getSum()).isEqualTo(UPDATED_SUM);
        assertThat(testOrder.getDeliveryAdress()).isEqualTo(UPDATED_DELIVERY_ADRESS);
        assertThat(testOrder.getDeliveryDateTime()).isEqualTo(UPDATED_DELIVERY_DATE_TIME);
        assertThat(testOrder.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testOrder.getBillingMethod()).isEqualTo(UPDATED_BILLING_METHOD);
        assertThat(testOrder.getOrderState()).isEqualTo(UPDATED_ORDER_STATE);
    }

    @Test
    @Transactional
    void putNonExistingOrder() throws Exception {
        int databaseSizeBeforeUpdate = orderRepository.findAll().size();
        order.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, order.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(order))
            )
            .andExpect(status().isBadRequest());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrder() throws Exception {
        int databaseSizeBeforeUpdate = orderRepository.findAll().size();
        order.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(order))
            )
            .andExpect(status().isBadRequest());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrder() throws Exception {
        int databaseSizeBeforeUpdate = orderRepository.findAll().size();
        order.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(order)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOrderWithPatch() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        int databaseSizeBeforeUpdate = orderRepository.findAll().size();

        // Update the order using partial update
        Order partialUpdatedOrder = new Order();
        partialUpdatedOrder.setId(order.getId());

        partialUpdatedOrder
            .sum(UPDATED_SUM)
            .deliveryDateTime(UPDATED_DELIVERY_DATE_TIME)
            .quantity(UPDATED_QUANTITY)
            .billingMethod(UPDATED_BILLING_METHOD)
            .orderState(UPDATED_ORDER_STATE);

        restOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrder.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrder))
            )
            .andExpect(status().isOk());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
        Order testOrder = orderList.get(orderList.size() - 1);
        assertThat(testOrder.getSum()).isEqualTo(UPDATED_SUM);
        assertThat(testOrder.getDeliveryAdress()).isEqualTo(DEFAULT_DELIVERY_ADRESS);
        assertThat(testOrder.getDeliveryDateTime()).isEqualTo(UPDATED_DELIVERY_DATE_TIME);
        assertThat(testOrder.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testOrder.getBillingMethod()).isEqualTo(UPDATED_BILLING_METHOD);
        assertThat(testOrder.getOrderState()).isEqualTo(UPDATED_ORDER_STATE);
    }

    @Test
    @Transactional
    void fullUpdateOrderWithPatch() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        int databaseSizeBeforeUpdate = orderRepository.findAll().size();

        // Update the order using partial update
        Order partialUpdatedOrder = new Order();
        partialUpdatedOrder.setId(order.getId());

        partialUpdatedOrder
            .sum(UPDATED_SUM)
            .deliveryAdress(UPDATED_DELIVERY_ADRESS)
            .deliveryDateTime(UPDATED_DELIVERY_DATE_TIME)
            .quantity(UPDATED_QUANTITY)
            .billingMethod(UPDATED_BILLING_METHOD)
            .orderState(UPDATED_ORDER_STATE);

        restOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrder.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOrder))
            )
            .andExpect(status().isOk());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
        Order testOrder = orderList.get(orderList.size() - 1);
        assertThat(testOrder.getSum()).isEqualTo(UPDATED_SUM);
        assertThat(testOrder.getDeliveryAdress()).isEqualTo(UPDATED_DELIVERY_ADRESS);
        assertThat(testOrder.getDeliveryDateTime()).isEqualTo(UPDATED_DELIVERY_DATE_TIME);
        assertThat(testOrder.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testOrder.getBillingMethod()).isEqualTo(UPDATED_BILLING_METHOD);
        assertThat(testOrder.getOrderState()).isEqualTo(UPDATED_ORDER_STATE);
    }

    @Test
    @Transactional
    void patchNonExistingOrder() throws Exception {
        int databaseSizeBeforeUpdate = orderRepository.findAll().size();
        order.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, order.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(order))
            )
            .andExpect(status().isBadRequest());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrder() throws Exception {
        int databaseSizeBeforeUpdate = orderRepository.findAll().size();
        order.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(order))
            )
            .andExpect(status().isBadRequest());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrder() throws Exception {
        int databaseSizeBeforeUpdate = orderRepository.findAll().size();
        order.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrderMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(order)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Order in the database
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOrder() throws Exception {
        // Initialize the database
        orderRepository.saveAndFlush(order);

        int databaseSizeBeforeDelete = orderRepository.findAll().size();

        // Delete the order
        restOrderMockMvc
            .perform(delete(ENTITY_API_URL_ID, order.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Order> orderList = orderRepository.findAll();
        assertThat(orderList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
