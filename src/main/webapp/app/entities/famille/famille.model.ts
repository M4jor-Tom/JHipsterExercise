import { ISousFamille } from 'app/entities/sous-famille/sous-famille.model';

export interface IFamille {
  id?: number;
  iDFamille?: number | null;
  famille?: string | null;
  sousFamille?: ISousFamille | null;
}

export class Famille implements IFamille {
  constructor(
    public id?: number,
    public iDFamille?: number | null,
    public famille?: string | null,
    public sousFamille?: ISousFamille | null
  ) {}
}

export function getFamilleIdentifier(famille: IFamille): number | undefined {
  return famille.id;
}
