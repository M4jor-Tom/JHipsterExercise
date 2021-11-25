package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Caracteristique.
 */
@Entity
@Table(name = "caracteristique")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Caracteristique implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "i_d_caracteristique")
    private Integer iDCaracteristique;

    @Column(name = "marque")
    private String marque;

    @Column(name = "modele")
    private String modele;

    @Column(name = "taille")
    private String taille;

    @Column(name = "couleur")
    private String couleur;

    @JsonIgnoreProperties(value = { "caracteristique", "famille", "commandes" }, allowSetters = true)
    @OneToOne(mappedBy = "caracteristique")
    private Produit produit;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Caracteristique id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getiDCaracteristique() {
        return this.iDCaracteristique;
    }

    public Caracteristique iDCaracteristique(Integer iDCaracteristique) {
        this.setiDCaracteristique(iDCaracteristique);
        return this;
    }

    public void setiDCaracteristique(Integer iDCaracteristique) {
        this.iDCaracteristique = iDCaracteristique;
    }

    public String getMarque() {
        return this.marque;
    }

    public Caracteristique marque(String marque) {
        this.setMarque(marque);
        return this;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public String getModele() {
        return this.modele;
    }

    public Caracteristique modele(String modele) {
        this.setModele(modele);
        return this;
    }

    public void setModele(String modele) {
        this.modele = modele;
    }

    public String getTaille() {
        return this.taille;
    }

    public Caracteristique taille(String taille) {
        this.setTaille(taille);
        return this;
    }

    public void setTaille(String taille) {
        this.taille = taille;
    }

    public String getCouleur() {
        return this.couleur;
    }

    public Caracteristique couleur(String couleur) {
        this.setCouleur(couleur);
        return this;
    }

    public void setCouleur(String couleur) {
        this.couleur = couleur;
    }

    public Produit getProduit() {
        return this.produit;
    }

    public void setProduit(Produit produit) {
        if (this.produit != null) {
            this.produit.setCaracteristique(null);
        }
        if (produit != null) {
            produit.setCaracteristique(this);
        }
        this.produit = produit;
    }

    public Caracteristique produit(Produit produit) {
        this.setProduit(produit);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Caracteristique)) {
            return false;
        }
        return id != null && id.equals(((Caracteristique) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Caracteristique{" +
            "id=" + getId() +
            ", iDCaracteristique=" + getiDCaracteristique() +
            ", marque='" + getMarque() + "'" +
            ", modele='" + getModele() + "'" +
            ", taille='" + getTaille() + "'" +
            ", couleur='" + getCouleur() + "'" +
            "}";
    }
}
