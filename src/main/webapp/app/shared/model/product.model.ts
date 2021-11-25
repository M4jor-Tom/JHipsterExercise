import { IModel } from '@/shared/model/model.model';
import { IOrder } from '@/shared/model/order.model';

import { Color } from '@/shared/model/enumerations/color.model';
export interface IProduct {
  id?: number;
  name?: string | null;
  description?: string | null;
  photoId?: number | null;
  stock?: number | null;
  tag?: string | null;
  tariff?: number | null;
  color?: Color | null;
  model?: IModel | null;
  orders?: IOrder[] | null;
}

export class Product implements IProduct {
  constructor(
    public id?: number,
    public name?: string | null,
    public description?: string | null,
    public photoId?: number | null,
    public stock?: number | null,
    public tag?: string | null,
    public tariff?: number | null,
    public color?: Color | null,
    public model?: IModel | null,
    public orders?: IOrder[] | null
  ) {}
}
