package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Produit.
 */
@Entity
@Table(name = "produit")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Produit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "i_d_produit")
    private Integer iDProduit;

    @Column(name = "nom")
    private String nom;

    @Column(name = "description")
    private String description;

    @Column(name = "photo")
    private Integer photo;

    @Column(name = "stock")
    private Integer stock;

    @Column(name = "tags")
    private String tags;

    @Column(name = "tarif")
    private Float tarif;

    @JsonIgnoreProperties(value = { "produit" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Caracteristique caracteristique;

    @JsonIgnoreProperties(value = { "sousFamille" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Famille famille;

    @ManyToMany(mappedBy = "produits")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "produits", "client" }, allowSetters = true)
    private Set<Commande> commandes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Produit id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getiDProduit() {
        return this.iDProduit;
    }

    public Produit iDProduit(Integer iDProduit) {
        this.setiDProduit(iDProduit);
        return this;
    }

    public void setiDProduit(Integer iDProduit) {
        this.iDProduit = iDProduit;
    }

    public String getNom() {
        return this.nom;
    }

    public Produit nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return this.description;
    }

    public Produit description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPhoto() {
        return this.photo;
    }

    public Produit photo(Integer photo) {
        this.setPhoto(photo);
        return this;
    }

    public void setPhoto(Integer photo) {
        this.photo = photo;
    }

    public Integer getStock() {
        return this.stock;
    }

    public Produit stock(Integer stock) {
        this.setStock(stock);
        return this;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getTags() {
        return this.tags;
    }

    public Produit tags(String tags) {
        this.setTags(tags);
        return this;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Float getTarif() {
        return this.tarif;
    }

    public Produit tarif(Float tarif) {
        this.setTarif(tarif);
        return this;
    }

    public void setTarif(Float tarif) {
        this.tarif = tarif;
    }

    public Caracteristique getCaracteristique() {
        return this.caracteristique;
    }

    public void setCaracteristique(Caracteristique caracteristique) {
        this.caracteristique = caracteristique;
    }

    public Produit caracteristique(Caracteristique caracteristique) {
        this.setCaracteristique(caracteristique);
        return this;
    }

    public Famille getFamille() {
        return this.famille;
    }

    public void setFamille(Famille famille) {
        this.famille = famille;
    }

    public Produit famille(Famille famille) {
        this.setFamille(famille);
        return this;
    }

    public Set<Commande> getCommandes() {
        return this.commandes;
    }

    public void setCommandes(Set<Commande> commandes) {
        if (this.commandes != null) {
            this.commandes.forEach(i -> i.removeProduit(this));
        }
        if (commandes != null) {
            commandes.forEach(i -> i.addProduit(this));
        }
        this.commandes = commandes;
    }

    public Produit commandes(Set<Commande> commandes) {
        this.setCommandes(commandes);
        return this;
    }

    public Produit addCommande(Commande commande) {
        this.commandes.add(commande);
        commande.getProduits().add(this);
        return this;
    }

    public Produit removeCommande(Commande commande) {
        this.commandes.remove(commande);
        commande.getProduits().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Produit)) {
            return false;
        }
        return id != null && id.equals(((Produit) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Produit{" +
            "id=" + getId() +
            ", iDProduit=" + getiDProduit() +
            ", nom='" + getNom() + "'" +
            ", description='" + getDescription() + "'" +
            ", photo=" + getPhoto() +
            ", stock=" + getStock() +
            ", tags='" + getTags() + "'" +
            ", tarif=" + getTarif() +
            "}";
    }
}
