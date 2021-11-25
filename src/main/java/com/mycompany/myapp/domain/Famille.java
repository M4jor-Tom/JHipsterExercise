package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Famille.
 */
@Entity
@Table(name = "famille")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Famille implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "i_d_famille")
    private Integer iDFamille;

    @Column(name = "famille")
    private String famille;

    @JsonIgnoreProperties(value = { "famille" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private SousFamille sousFamille;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Famille id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getiDFamille() {
        return this.iDFamille;
    }

    public Famille iDFamille(Integer iDFamille) {
        this.setiDFamille(iDFamille);
        return this;
    }

    public void setiDFamille(Integer iDFamille) {
        this.iDFamille = iDFamille;
    }

    public String getFamille() {
        return this.famille;
    }

    public Famille famille(String famille) {
        this.setFamille(famille);
        return this;
    }

    public void setFamille(String famille) {
        this.famille = famille;
    }

    public SousFamille getSousFamille() {
        return this.sousFamille;
    }

    public void setSousFamille(SousFamille sousFamille) {
        this.sousFamille = sousFamille;
    }

    public Famille sousFamille(SousFamille sousFamille) {
        this.setSousFamille(sousFamille);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Famille)) {
            return false;
        }
        return id != null && id.equals(((Famille) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Famille{" +
            "id=" + getId() +
            ", iDFamille=" + getiDFamille() +
            ", famille='" + getFamille() + "'" +
            "}";
    }
}
