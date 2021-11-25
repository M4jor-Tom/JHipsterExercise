package com.mycompany.myapp.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Connexion.
 */
@Entity
@Table(name = "connexion")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Connexion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "i_d_connexion")
    private Integer iDConnexion;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Connexion id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getiDConnexion() {
        return this.iDConnexion;
    }

    public Connexion iDConnexion(Integer iDConnexion) {
        this.setiDConnexion(iDConnexion);
        return this;
    }

    public void setiDConnexion(Integer iDConnexion) {
        this.iDConnexion = iDConnexion;
    }

    public String getUsername() {
        return this.username;
    }

    public Connexion username(String username) {
        this.setUsername(username);
        return this;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public Connexion password(String password) {
        this.setPassword(password);
        return this;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Connexion)) {
            return false;
        }
        return id != null && id.equals(((Connexion) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Connexion{" +
            "id=" + getId() +
            ", iDConnexion=" + getiDConnexion() +
            ", username='" + getUsername() + "'" +
            ", password='" + getPassword() + "'" +
            "}";
    }
}
