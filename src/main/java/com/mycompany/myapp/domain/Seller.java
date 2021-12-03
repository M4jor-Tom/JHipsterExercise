package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Seller.
 */
@Entity
@Table(name = "seller")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Seller implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "social_reason", nullable = false)
    private String socialReason;

    @NotNull
    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "siret_number")
    private String siretNumber;

    @Column(name = "phone")
    private Long phone;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(mappedBy = "seller")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "subFamily", "brand", "seller", "tags", "orders" }, allowSetters = true)
    private Set<Product> products = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Seller id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSocialReason() {
        return this.socialReason;
    }

    public Seller socialReason(String socialReason) {
        this.setSocialReason(socialReason);
        return this;
    }

    public void setSocialReason(String socialReason) {
        this.socialReason = socialReason;
    }

    public String getAddress() {
        return this.address;
    }

    public Seller address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSiretNumber() {
        return this.siretNumber;
    }

    public Seller siretNumber(String siretNumber) {
        this.setSiretNumber(siretNumber);
        return this;
    }

    public void setSiretNumber(String siretNumber) {
        this.siretNumber = siretNumber;
    }

    public Long getPhone() {
        return this.phone;
    }

    public Seller phone(Long phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(Long phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return this.email;
    }

    public Seller email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Seller user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<Product> getProducts() {
        return this.products;
    }

    public void setProducts(Set<Product> products) {
        if (this.products != null) {
            this.products.forEach(i -> i.setSeller(null));
        }
        if (products != null) {
            products.forEach(i -> i.setSeller(this));
        }
        this.products = products;
    }

    public Seller products(Set<Product> products) {
        this.setProducts(products);
        return this;
    }

    public Seller addProduct(Product product) {
        this.products.add(product);
        product.setSeller(this);
        return this;
    }

    public Seller removeProduct(Product product) {
        this.products.remove(product);
        product.setSeller(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Seller)) {
            return false;
        }
        return id != null && id.equals(((Seller) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Seller{" +
            "id=" + getId() +
            ", socialReason='" + getSocialReason() + "'" +
            ", address='" + getAddress() + "'" +
            ", siretNumber='" + getSiretNumber() + "'" +
            ", phone=" + getPhone() +
            ", email='" + getEmail() + "'" +
            "}";
    }
}
