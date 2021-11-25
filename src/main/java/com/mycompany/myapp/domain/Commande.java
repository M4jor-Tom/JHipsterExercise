package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Commande.
 */
@Entity
@Table(name = "commande")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Commande implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "i_d_commande")
    private Integer iDCommande;

    @Column(name = "liste_produit")
    private Integer listeProduit;

    @Column(name = "somme")
    private Float somme;

    @Column(name = "addresse_livraison")
    private String addresseLivraison;

    @Column(name = "mode_paiement")
    private Integer modePaiement;

    @Column(name = "date_livraison")
    private ZonedDateTime dateLivraison;

    @ManyToMany
    @JoinTable(
        name = "rel_commande__produit",
        joinColumns = @JoinColumn(name = "commande_id"),
        inverseJoinColumns = @JoinColumn(name = "produit_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "caracteristique", "famille", "commandes" }, allowSetters = true)
    private Set<Produit> produits = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "connexion", "commandes" }, allowSetters = true)
    private Client client;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Commande id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getiDCommande() {
        return this.iDCommande;
    }

    public Commande iDCommande(Integer iDCommande) {
        this.setiDCommande(iDCommande);
        return this;
    }

    public void setiDCommande(Integer iDCommande) {
        this.iDCommande = iDCommande;
    }

    public Integer getListeProduit() {
        return this.listeProduit;
    }

    public Commande listeProduit(Integer listeProduit) {
        this.setListeProduit(listeProduit);
        return this;
    }

    public void setListeProduit(Integer listeProduit) {
        this.listeProduit = listeProduit;
    }

    public Float getSomme() {
        return this.somme;
    }

    public Commande somme(Float somme) {
        this.setSomme(somme);
        return this;
    }

    public void setSomme(Float somme) {
        this.somme = somme;
    }

    public String getAddresseLivraison() {
        return this.addresseLivraison;
    }

    public Commande addresseLivraison(String addresseLivraison) {
        this.setAddresseLivraison(addresseLivraison);
        return this;
    }

    public void setAddresseLivraison(String addresseLivraison) {
        this.addresseLivraison = addresseLivraison;
    }

    public Integer getModePaiement() {
        return this.modePaiement;
    }

    public Commande modePaiement(Integer modePaiement) {
        this.setModePaiement(modePaiement);
        return this;
    }

    public void setModePaiement(Integer modePaiement) {
        this.modePaiement = modePaiement;
    }

    public ZonedDateTime getDateLivraison() {
        return this.dateLivraison;
    }

    public Commande dateLivraison(ZonedDateTime dateLivraison) {
        this.setDateLivraison(dateLivraison);
        return this;
    }

    public void setDateLivraison(ZonedDateTime dateLivraison) {
        this.dateLivraison = dateLivraison;
    }

    public Set<Produit> getProduits() {
        return this.produits;
    }

    public void setProduits(Set<Produit> produits) {
        this.produits = produits;
    }

    public Commande produits(Set<Produit> produits) {
        this.setProduits(produits);
        return this;
    }

    public Commande addProduit(Produit produit) {
        this.produits.add(produit);
        produit.getCommandes().add(this);
        return this;
    }

    public Commande removeProduit(Produit produit) {
        this.produits.remove(produit);
        produit.getCommandes().remove(this);
        return this;
    }

    public Client getClient() {
        return this.client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Commande client(Client client) {
        this.setClient(client);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Commande)) {
            return false;
        }
        return id != null && id.equals(((Commande) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Commande{" +
            "id=" + getId() +
            ", iDCommande=" + getiDCommande() +
            ", listeProduit=" + getListeProduit() +
            ", somme=" + getSomme() +
            ", addresseLivraison='" + getAddresseLivraison() + "'" +
            ", modePaiement=" + getModePaiement() +
            ", dateLivraison='" + getDateLivraison() + "'" +
            "}";
    }
}
