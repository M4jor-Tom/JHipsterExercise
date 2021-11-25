import * as dayjs from 'dayjs';
import { IConnexion } from 'app/entities/connexion/connexion.model';
import { ICommande } from 'app/entities/commande/commande.model';

export interface IClient {
  id?: number;
  iDClient?: number | null;
  dateAjout?: dayjs.Dayjs | null;
  nom?: string | null;
  prenom?: string | null;
  email?: string | null;
  telephone?: number | null;
  adresse?: string | null;
  pays?: string | null;
  codePostale?: number | null;
  connexion?: IConnexion | null;
  commandes?: ICommande[] | null;
}

export class Client implements IClient {
  constructor(
    public id?: number,
    public iDClient?: number | null,
    public dateAjout?: dayjs.Dayjs | null,
    public nom?: string | null,
    public prenom?: string | null,
    public email?: string | null,
    public telephone?: number | null,
    public adresse?: string | null,
    public pays?: string | null,
    public codePostale?: number | null,
    public connexion?: IConnexion | null,
    public commandes?: ICommande[] | null
  ) {}
}

export function getClientIdentifier(client: IClient): number | undefined {
  return client.id;
}
