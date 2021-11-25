export interface IConnexion {
  id?: number;
  iDConnexion?: number | null;
  username?: string | null;
  password?: string | null;
}

export class Connexion implements IConnexion {
  constructor(public id?: number, public iDConnexion?: number | null, public username?: string | null, public password?: string | null) {}
}

export function getConnexionIdentifier(connexion: IConnexion): number | undefined {
  return connexion.id;
}
