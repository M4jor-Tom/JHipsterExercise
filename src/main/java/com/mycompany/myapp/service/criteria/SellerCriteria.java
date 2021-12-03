package com.mycompany.myapp.service.criteria;

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

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Seller} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.SellerResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /sellers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SellerCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter socialReason;

    private StringFilter address;

    private StringFilter siretNumber;

    private LongFilter phone;

    private StringFilter email;

    private LongFilter userId;

    private LongFilter productId;

    private Boolean distinct;

    public SellerCriteria() {}

    public SellerCriteria(SellerCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.socialReason = other.socialReason == null ? null : other.socialReason.copy();
        this.address = other.address == null ? null : other.address.copy();
        this.siretNumber = other.siretNumber == null ? null : other.siretNumber.copy();
        this.phone = other.phone == null ? null : other.phone.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.productId = other.productId == null ? null : other.productId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public SellerCriteria copy() {
        return new SellerCriteria(this);
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

    public StringFilter getSocialReason() {
        return socialReason;
    }

    public StringFilter socialReason() {
        if (socialReason == null) {
            socialReason = new StringFilter();
        }
        return socialReason;
    }

    public void setSocialReason(StringFilter socialReason) {
        this.socialReason = socialReason;
    }

    public StringFilter getAddress() {
        return address;
    }

    public StringFilter address() {
        if (address == null) {
            address = new StringFilter();
        }
        return address;
    }

    public void setAddress(StringFilter address) {
        this.address = address;
    }

    public StringFilter getSiretNumber() {
        return siretNumber;
    }

    public StringFilter siretNumber() {
        if (siretNumber == null) {
            siretNumber = new StringFilter();
        }
        return siretNumber;
    }

    public void setSiretNumber(StringFilter siretNumber) {
        this.siretNumber = siretNumber;
    }

    public LongFilter getPhone() {
        return phone;
    }

    public LongFilter phone() {
        if (phone == null) {
            phone = new LongFilter();
        }
        return phone;
    }

    public void setPhone(LongFilter phone) {
        this.phone = phone;
    }

    public StringFilter getEmail() {
        return email;
    }

    public StringFilter email() {
        if (email == null) {
            email = new StringFilter();
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public LongFilter userId() {
        if (userId == null) {
            userId = new LongFilter();
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getProductId() {
        return productId;
    }

    public LongFilter productId() {
        if (productId == null) {
            productId = new LongFilter();
        }
        return productId;
    }

    public void setProductId(LongFilter productId) {
        this.productId = productId;
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
        final SellerCriteria that = (SellerCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(socialReason, that.socialReason) &&
            Objects.equals(address, that.address) &&
            Objects.equals(siretNumber, that.siretNumber) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(email, that.email) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(productId, that.productId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, socialReason, address, siretNumber, phone, email, userId, productId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SellerCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (socialReason != null ? "socialReason=" + socialReason + ", " : "") +
            (address != null ? "address=" + address + ", " : "") +
            (siretNumber != null ? "siretNumber=" + siretNumber + ", " : "") +
            (phone != null ? "phone=" + phone + ", " : "") +
            (email != null ? "email=" + email + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (productId != null ? "productId=" + productId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
