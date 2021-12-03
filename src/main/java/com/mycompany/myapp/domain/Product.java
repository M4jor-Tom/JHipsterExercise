package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.Color;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Product.
 */
@Entity
@Table(name = "product")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "photo_id")
    private Long photoId;

    @NotNull
    @Column(name = "stock", nullable = false)
    private Long stock;

    @NotNull
    @Column(name = "price", nullable = false)
    private Double price;

    @NotNull
    @Column(name = "model_name", nullable = false)
    private String modelName;

    @Enumerated(EnumType.STRING)
    @Column(name = "color")
    private Color color;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "family" }, allowSetters = true)
    private SubFamily subFamily;

    @ManyToOne(optional = false)
    @NotNull
    private Brand brand;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "connection", "products" }, allowSetters = true)
    private Seller seller;

    @ManyToMany
    @JoinTable(
        name = "rel_product__tags",
        joinColumns = @JoinColumn(name = "product_id"),
        inverseJoinColumns = @JoinColumn(name = "tags_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "products" }, allowSetters = true)
    private Set<Tag> tags = new HashSet<>();

    @ManyToMany(mappedBy = "products")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "products", "client" }, allowSetters = true)
    private Set<Order> orders = new HashSet<>();

    public String getBrandNameWithModelName() {
        return getBrand().getName() + " " + getModelName();
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Product id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public Product description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getPhotoId() {
        return this.photoId;
    }

    public Product photoId(Long photoId) {
        this.setPhotoId(photoId);
        return this;
    }

    public void setPhotoId(Long photoId) {
        this.photoId = photoId;
    }

    public Long getStock() {
        return this.stock;
    }

    public Product stock(Long stock) {
        this.setStock(stock);
        return this;
    }

    public void setStock(Long stock) {
        this.stock = stock;
    }

    public Double getPrice() {
        return this.price;
    }

    public Product price(Double price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getModelName() {
        return this.modelName;
    }

    public Product modelName(String modelName) {
        this.setModelName(modelName);
        return this;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public Color getColor() {
        return this.color;
    }

    public Product color(Color color) {
        this.setColor(color);
        return this;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public SubFamily getSubFamily() {
        return this.subFamily;
    }

    public void setSubFamily(SubFamily subFamily) {
        this.subFamily = subFamily;
    }

    public Product subFamily(SubFamily subFamily) {
        this.setSubFamily(subFamily);
        return this;
    }

    public Brand getBrand() {
        return this.brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    public Product brand(Brand brand) {
        this.setBrand(brand);
        return this;
    }

    public Seller getSeller() {
        return this.seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    public Product seller(Seller seller) {
        this.setSeller(seller);
        return this;
    }

    public Set<Tag> getTags() {
        return this.tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Product tags(Set<Tag> tags) {
        this.setTags(tags);
        return this;
    }

    public Product addTags(Tag tag) {
        this.tags.add(tag);
        tag.getProducts().add(this);
        return this;
    }

    public Product removeTags(Tag tag) {
        this.tags.remove(tag);
        tag.getProducts().remove(this);
        return this;
    }

    public Set<Order> getOrders() {
        return this.orders;
    }

    public void setOrders(Set<Order> orders) {
        if (this.orders != null) {
            this.orders.forEach(i -> i.removeProducts(this));
        }
        if (orders != null) {
            orders.forEach(i -> i.addProducts(this));
        }
        this.orders = orders;
    }

    public Product orders(Set<Order> orders) {
        this.setOrders(orders);
        return this;
    }

    public Product addOrder(Order order) {
        this.orders.add(order);
        order.getProducts().add(this);
        return this;
    }

    public Product removeOrder(Order order) {
        this.orders.remove(order);
        order.getProducts().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product)) {
            return false;
        }
        return id != null && id.equals(((Product) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Product{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", photoId=" + getPhotoId() +
            ", stock=" + getStock() +
            ", price=" + getPrice() +
            ", modelName='" + getModelName() + "'" +
            ", color='" + getColor() + "'" +
            "}";
    }
}
