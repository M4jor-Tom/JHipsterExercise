import { IBrand } from '@/shared/model/brand.model';
import { IFamily } from '@/shared/model/family.model';

export interface IModel {
  id?: number;
  name?: string | null;
  brand?: IBrand | null;
  family?: IFamily | null;
}

export class Model implements IModel {
  constructor(public id?: number, public name?: string | null, public brand?: IBrand | null, public family?: IFamily | null) {}
}
