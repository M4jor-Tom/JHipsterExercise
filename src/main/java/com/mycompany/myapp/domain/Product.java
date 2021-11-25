package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.Color;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
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

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "photo_id")
    private Long photoId;

    @Column(name = "stock")
    private Long stock;

    @Column(name = "tag")
    private String tag;

    @Column(name = "tariff")
    private Double tariff;

    @Enumerated(EnumType.STRING)
    @Column(name = "color")
    private Color color;

    @ManyToOne
    @JsonIgnoreProperties(value = { "brand", "family" }, allowSetters = true)
    private Model model;

    @ManyToMany(mappedBy = "products")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "products", "client" }, allowSetters = true)
    private Set<Order> orders = new HashSet<>();

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

    public String getName() {
        return this.name;
    }

    public Product name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getTag() {
        return this.tag;
    }

    public Product tag(String tag) {
        this.setTag(tag);
        return this;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Double getTariff() {
        return this.tariff;
    }

    public Product tariff(Double tariff) {
        this.setTariff(tariff);
        return this;
    }

    public void setTariff(Double tariff) {
        this.tariff = tariff;
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

    public Model getModel() {
        return this.model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public Product model(Model model) {
        this.setModel(model);
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
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", photoId=" + getPhotoId() +
            ", stock=" + getStock() +
            ", tag='" + getTag() + "'" +
            ", tariff=" + getTariff() +
            ", color='" + getColor() + "'" +
            "}";
    }
}
