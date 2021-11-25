import * as dayjs from 'dayjs';
import { IProduit } from 'app/entities/produit/produit.model';
import { IClient } from 'app/entities/client/client.model';

export interface ICommande {
  id?: number;
  iDCommande?: number | null;
  listeProduit?: number | null;
  somme?: number | null;
  addresseLivraison?: string | null;
  modePaiement?: number | null;
  dateLivraison?: dayjs.Dayjs | null;
  produits?: IProduit[] | null;
  client?: IClient | null;
}

export class Commande implements ICommande {
  constructor(
    public id?: number,
    public iDCommande?: number | null,
    public listeProduit?: number | null,
    public somme?: number | null,
    public addresseLivraison?: string | null,
    public modePaiement?: number | null,
    public dateLivraison?: dayjs.Dayjs | null,
    public produits?: IProduit[] | null,
    public client?: IClient | null
  ) {}
}

export function getCommandeIdentifier(commande: ICommande): number | undefined {
  return commande.id;
}
