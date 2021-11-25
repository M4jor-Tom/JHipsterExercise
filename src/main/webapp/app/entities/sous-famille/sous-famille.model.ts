import { IFamille } from 'app/entities/famille/famille.model';

export interface ISousFamille {
  id?: number;
  iDSousFamille?: number | null;
  sousFamille?: string | null;
  famille?: IFamille | null;
}

export class SousFamille implements ISousFamille {
  constructor(
    public id?: number,
    public iDSousFamille?: number | null,
    public sousFamille?: string | null,
    public famille?: IFamille | null
  ) {}
}

export function getSousFamilleIdentifier(sousFamille: ISousFamille): number | undefined {
  return sousFamille.id;
}
