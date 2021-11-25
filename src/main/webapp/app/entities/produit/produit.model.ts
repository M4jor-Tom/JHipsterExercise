import { ICaracteristique } from 'app/entities/caracteristique/caracteristique.model';
import { IFamille } from 'app/entities/famille/famille.model';
import { ICommande } from 'app/entities/commande/commande.model';

export interface IProduit {
  id?: number;
  iDProduit?: number | null;
  nom?: string | null;
  description?: string | null;
  photo?: number | null;
  stock?: number | null;
  tags?: string | null;
  tarif?: number | null;
  caracteristique?: ICaracteristique | null;
  famille?: IFamille | null;
  commandes?: ICommande[] | null;
}

export class Produit implements IProduit {
  constructor(
    public id?: number,
    public iDProduit?: number | null,
    public nom?: string | null,
    public description?: string | null,
    public photo?: number | null,
    public stock?: number | null,
    public tags?: string | null,
    public tarif?: number | null,
    public caracteristique?: ICaracteristique | null,
    public famille?: IFamille | null,
    public commandes?: ICommande[] | null
  ) {}
}

export function getProduitIdentifier(produit: IProduit): number | undefined {
  return produit.id;
}
