import { IFamily } from '@/shared/model/family.model';

export interface ISubFamily {
  id?: number;
  name?: string;
  family?: IFamily | null;
}

export class SubFamily implements ISubFamily {
  constructor(public id?: number, public name?: string, public family?: IFamily | null) {}
}
