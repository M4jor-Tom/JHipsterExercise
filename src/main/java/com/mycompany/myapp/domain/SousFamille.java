package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SousFamille.
 */
@Entity
@Table(name = "sous_famille")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SousFamille implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "i_d_sous_famille")
    private Integer iDSousFamille;

    @Column(name = "sous_famille")
    private String sousFamille;

    @JsonIgnoreProperties(value = { "sousFamille" }, allowSetters = true)
    @OneToOne(mappedBy = "sousFamille")
    private Famille famille;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SousFamille id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getiDSousFamille() {
        return this.iDSousFamille;
    }

    public SousFamille iDSousFamille(Integer iDSousFamille) {
        this.setiDSousFamille(iDSousFamille);
        return this;
    }

    public void setiDSousFamille(Integer iDSousFamille) {
        this.iDSousFamille = iDSousFamille;
    }

    public String getSousFamille() {
        return this.sousFamille;
    }

    public SousFamille sousFamille(String sousFamille) {
        this.setSousFamille(sousFamille);
        return this;
    }

    public void setSousFamille(String sousFamille) {
        this.sousFamille = sousFamille;
    }

    public Famille getFamille() {
        return this.famille;
    }

    public void setFamille(Famille famille) {
        if (this.famille != null) {
            this.famille.setSousFamille(null);
        }
        if (famille != null) {
            famille.setSousFamille(this);
        }
        this.famille = famille;
    }

    public SousFamille famille(Famille famille) {
        this.setFamille(famille);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SousFamille)) {
            return false;
        }
        return id != null && id.equals(((SousFamille) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SousFamille{" +
            "id=" + getId() +
            ", iDSousFamille=" + getiDSousFamille() +
            ", sousFamille='" + getSousFamille() + "'" +
            "}";
    }
}
