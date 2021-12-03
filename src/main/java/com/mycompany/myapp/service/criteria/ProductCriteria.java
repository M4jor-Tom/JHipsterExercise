package com.mycompany.myapp.service.criteria;

import com.mycompany.myapp.domain.enumeration.Color;
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
 * Criteria class for the {@link com.mycompany.myapp.domain.Product} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.ProductResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /products?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ProductCriteria implements Serializable, Criteria {

    /**
     * Class for filtering Color
     */
    public static class ColorFilter extends Filter<Color> {

        public ColorFilter() {}

        public ColorFilter(ColorFilter filter) {
            super(filter);
        }

        @Override
        public ColorFilter copy() {
            return new ColorFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter description;

    private LongFilter photoId;

    private LongFilter stock;

    private DoubleFilter price;

    private StringFilter modelName;

    private ColorFilter color;

    private LongFilter subFamilyId;

    private LongFilter brandId;

    private LongFilter sellerId;

    private LongFilter tagsId;

    private LongFilter orderId;

    private Boolean distinct;

    public ProductCriteria() {}

    public ProductCriteria(ProductCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.photoId = other.photoId == null ? null : other.photoId.copy();
        this.stock = other.stock == null ? null : other.stock.copy();
        this.price = other.price == null ? null : other.price.copy();
        this.modelName = other.modelName == null ? null : other.modelName.copy();
        this.color = other.color == null ? null : other.color.copy();
        this.subFamilyId = other.subFamilyId == null ? null : other.subFamilyId.copy();
        this.brandId = other.brandId == null ? null : other.brandId.copy();
        this.sellerId = other.sellerId == null ? null : other.sellerId.copy();
        this.tagsId = other.tagsId == null ? null : other.tagsId.copy();
        this.orderId = other.orderId == null ? null : other.orderId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ProductCriteria copy() {
        return new ProductCriteria(this);
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

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public LongFilter getPhotoId() {
        return photoId;
    }

    public LongFilter photoId() {
        if (photoId == null) {
            photoId = new LongFilter();
        }
        return photoId;
    }

    public void setPhotoId(LongFilter photoId) {
        this.photoId = photoId;
    }

    public LongFilter getStock() {
        return stock;
    }

    public LongFilter stock() {
        if (stock == null) {
            stock = new LongFilter();
        }
        return stock;
    }

    public void setStock(LongFilter stock) {
        this.stock = stock;
    }

    public DoubleFilter getPrice() {
        return price;
    }

    public DoubleFilter price() {
        if (price == null) {
            price = new DoubleFilter();
        }
        return price;
    }

    public void setPrice(DoubleFilter price) {
        this.price = price;
    }

    public StringFilter getModelName() {
        return modelName;
    }

    public StringFilter modelName() {
        if (modelName == null) {
            modelName = new StringFilter();
        }
        return modelName;
    }

    public void setModelName(StringFilter modelName) {
        this.modelName = modelName;
    }

    public ColorFilter getColor() {
        return color;
    }

    public ColorFilter color() {
        if (color == null) {
            color = new ColorFilter();
        }
        return color;
    }

    public void setColor(ColorFilter color) {
        this.color = color;
    }

    public LongFilter getSubFamilyId() {
        return subFamilyId;
    }

    public LongFilter subFamilyId() {
        if (subFamilyId == null) {
            subFamilyId = new LongFilter();
        }
        return subFamilyId;
    }

    public void setSubFamilyId(LongFilter subFamilyId) {
        this.subFamilyId = subFamilyId;
    }

    public LongFilter getBrandId() {
        return brandId;
    }

    public LongFilter brandId() {
        if (brandId == null) {
            brandId = new LongFilter();
        }
        return brandId;
    }

    public void setBrandId(LongFilter brandId) {
        this.brandId = brandId;
    }

    public LongFilter getSellerId() {
        return sellerId;
    }

    public LongFilter sellerId() {
        if (sellerId == null) {
            sellerId = new LongFilter();
        }
        return sellerId;
    }

    public void setSellerId(LongFilter sellerId) {
        this.sellerId = sellerId;
    }

    public LongFilter getTagsId() {
        return tagsId;
    }

    public LongFilter tagsId() {
        if (tagsId == null) {
            tagsId = new LongFilter();
        }
        return tagsId;
    }

    public void setTagsId(LongFilter tagsId) {
        this.tagsId = tagsId;
    }

    public LongFilter getOrderId() {
        return orderId;
    }

    public LongFilter orderId() {
        if (orderId == null) {
            orderId = new LongFilter();
        }
        return orderId;
    }

    public void setOrderId(LongFilter orderId) {
        this.orderId = orderId;
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
        final ProductCriteria that = (ProductCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(description, that.description) &&
            Objects.equals(photoId, that.photoId) &&
            Objects.equals(stock, that.stock) &&
            Objects.equals(price, that.price) &&
            Objects.equals(modelName, that.modelName) &&
            Objects.equals(color, that.color) &&
            Objects.equals(subFamilyId, that.subFamilyId) &&
            Objects.equals(brandId, that.brandId) &&
            Objects.equals(sellerId, that.sellerId) &&
            Objects.equals(tagsId, that.tagsId) &&
            Objects.equals(orderId, that.orderId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            description,
            photoId,
            stock,
            price,
            modelName,
            color,
            subFamilyId,
            brandId,
            sellerId,
            tagsId,
            orderId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (photoId != null ? "photoId=" + photoId + ", " : "") +
            (stock != null ? "stock=" + stock + ", " : "") +
            (price != null ? "price=" + price + ", " : "") +
            (modelName != null ? "modelName=" + modelName + ", " : "") +
            (color != null ? "color=" + color + ", " : "") +
            (subFamilyId != null ? "subFamilyId=" + subFamilyId + ", " : "") +
            (brandId != null ? "brandId=" + brandId + ", " : "") +
            (sellerId != null ? "sellerId=" + sellerId + ", " : "") +
            (tagsId != null ? "tagsId=" + tagsId + ", " : "") +
            (orderId != null ? "orderId=" + orderId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
