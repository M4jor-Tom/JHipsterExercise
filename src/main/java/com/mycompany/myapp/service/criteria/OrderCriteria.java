package com.mycompany.myapp.service.criteria;

import com.mycompany.myapp.domain.enumeration.BillingMethod;
import com.mycompany.myapp.domain.enumeration.OrderState;
import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Order} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.OrderResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /orders?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class OrderCriteria implements Serializable, Criteria {

    /**
     * Class for filtering BillingMethod
     */
    public static class BillingMethodFilter extends Filter<BillingMethod> {

        public BillingMethodFilter() {}

        public BillingMethodFilter(BillingMethodFilter filter) {
            super(filter);
        }

        @Override
        public BillingMethodFilter copy() {
            return new BillingMethodFilter(this);
        }
    }

    /**
     * Class for filtering OrderState
     */
    public static class OrderStateFilter extends Filter<OrderState> {

        public OrderStateFilter() {}

        public OrderStateFilter(OrderStateFilter filter) {
            super(filter);
        }

        @Override
        public OrderStateFilter copy() {
            return new OrderStateFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private DoubleFilter sum;

    private StringFilter deliveryAdress;

    private ZonedDateTimeFilter deliveryDateTime;

    private LongFilter quantity;

    private BillingMethodFilter billingMethod;

    private OrderStateFilter orderState;

    private LongFilter productsId;

    private LongFilter clientId;

    private Boolean distinct;

    public OrderCriteria() {}

    public OrderCriteria(OrderCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.sum = other.sum == null ? null : other.sum.copy();
        this.deliveryAdress = other.deliveryAdress == null ? null : other.deliveryAdress.copy();
        this.deliveryDateTime = other.deliveryDateTime == null ? null : other.deliveryDateTime.copy();
        this.quantity = other.quantity == null ? null : other.quantity.copy();
        this.billingMethod = other.billingMethod == null ? null : other.billingMethod.copy();
        this.orderState = other.orderState == null ? null : other.orderState.copy();
        this.productsId = other.productsId == null ? null : other.productsId.copy();
        this.clientId = other.clientId == null ? null : other.clientId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public OrderCriteria copy() {
        return new OrderCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public DoubleFilter getSum() {
        return sum;
    }

    public DoubleFilter sum() {
        if (sum == null) {
            sum = new DoubleFilter();
        }
        return sum;
    }

    public void setSum(DoubleFilter sum) {
        this.sum = sum;
    }

    public StringFilter getDeliveryAdress() {
        return deliveryAdress;
    }

    public StringFilter deliveryAdress() {
        if (deliveryAdress == null) {
            deliveryAdress = new StringFilter();
        }
        return deliveryAdress;
    }

    public void setDeliveryAdress(StringFilter deliveryAdress) {
        this.deliveryAdress = deliveryAdress;
    }

    public ZonedDateTimeFilter getDeliveryDateTime() {
        return deliveryDateTime;
    }

    public ZonedDateTimeFilter deliveryDateTime() {
        if (deliveryDateTime == null) {
            deliveryDateTime = new ZonedDateTimeFilter();
        }
        return deliveryDateTime;
    }

    public void setDeliveryDateTime(ZonedDateTimeFilter deliveryDateTime) {
        this.deliveryDateTime = deliveryDateTime;
    }

    public LongFilter getQuantity() {
        return quantity;
    }

    public LongFilter quantity() {
        if (quantity == null) {
            quantity = new LongFilter();
        }
        return quantity;
    }

    public void setQuantity(LongFilter quantity) {
        this.quantity = quantity;
    }

    public BillingMethodFilter getBillingMethod() {
        return billingMethod;
    }

    public BillingMethodFilter billingMethod() {
        if (billingMethod == null) {
            billingMethod = new BillingMethodFilter();
        }
        return billingMethod;
    }

    public void setBillingMethod(BillingMethodFilter billingMethod) {
        this.billingMethod = billingMethod;
    }

    public OrderStateFilter getOrderState() {
        return orderState;
    }

    public OrderStateFilter orderState() {
        if (orderState == null) {
            orderState = new OrderStateFilter();
        }
        return orderState;
    }

    public void setOrderState(OrderStateFilter orderState) {
        this.orderState = orderState;
    }

    public LongFilter getProductsId() {
        return productsId;
    }

    public LongFilter productsId() {
        if (productsId == null) {
            productsId = new LongFilter();
        }
        return productsId;
    }

    public void setProductsId(LongFilter productsId) {
        this.productsId = productsId;
    }

    public LongFilter getClientId() {
        return clientId;
    }

    public LongFilter clientId() {
        if (clientId == null) {
            clientId = new LongFilter();
        }
        return clientId;
    }

    public void setClientId(LongFilter clientId) {
        this.clientId = clientId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final OrderCriteria that = (OrderCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(sum, that.sum) &&
            Objects.equals(deliveryAdress, that.deliveryAdress) &&
            Objects.equals(deliveryDateTime, that.deliveryDateTime) &&
            Objects.equals(quantity, that.quantity) &&
            Objects.equals(billingMethod, that.billingMethod) &&
            Objects.equals(orderState, that.orderState) &&
            Objects.equals(productsId, that.productsId) &&
            Objects.equals(clientId, that.clientId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sum, deliveryAdress, deliveryDateTime, quantity, billingMethod, orderState, productsId, clientId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrderCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (sum != null ? "sum=" + sum + ", " : "") +
            (deliveryAdress != null ? "deliveryAdress=" + deliveryAdress + ", " : "") +
            (deliveryDateTime != null ? "deliveryDateTime=" + deliveryDateTime + ", " : "") +
            (quantity != null ? "quantity=" + quantity + ", " : "") +
            (billingMethod != null ? "billingMethod=" + billingMethod + ", " : "") +
            (orderState != null ? "orderState=" + orderState + ", " : "") +
            (productsId != null ? "productsId=" + productsId + ", " : "") +
            (clientId != null ? "clientId=" + clientId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
