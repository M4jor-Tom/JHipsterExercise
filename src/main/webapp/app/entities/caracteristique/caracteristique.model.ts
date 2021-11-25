import { IProduit } from 'app/entities/produit/produit.model';

export interface ICaracteristique {
  id?: number;
  iDCaracteristique?: number | null;
  marque?: string | null;
  modele?: string | null;
  taille?: string | null;
  couleur?: string | null;
  produit?: IProduit | null;
}

export class Caracteristique implements ICaracteristique {
  constructor(
    public id?: number,
    public iDCaracteristique?: number | null,
    public marque?: string | null,
    public modele?: string | null,
    public taille?: string | null,
    public couleur?: string | null,
    public produit?: IProduit | null
  ) {}
}

export function getCaracteristiqueIdentifier(caracteristique: ICaracteristique): number | undefined {
  return caracteristique.id;
}
