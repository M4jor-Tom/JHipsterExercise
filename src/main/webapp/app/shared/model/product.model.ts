import { ISubFamily } from '@/shared/model/sub-family.model';
import { IBrand } from '@/shared/model/brand.model';
import { ISeller } from '@/shared/model/seller.model';
import { ITag } from '@/shared/model/tag.model';
import { IOrder } from '@/shared/model/order.model';

import { Color } from '@/shared/model/enumerations/color.model';
export interface IProduct {
  id?: number;
  description?: string | null;
  photoId?: number | null;
  stock?: number;
  price?: number;
  modelName?: string;
  color?: Color | null;
  subFamily?: ISubFamily;
  brand?: IBrand;
  seller?: ISeller;
  tags?: ITag[] | null;
  orders?: IOrder[] | null;
  brandNameWithModelName?: string;
}

export class Product implements IProduct {
  constructor(
    public id?: number,
    public description?: string | null,
    public photoId?: number | null,
    public stock?: number,
    public price?: number,
    public modelName?: string,
    public color?: Color | null,
    public subFamily?: ISubFamily,
    public brand?: IBrand,
    public seller?: ISeller,
    public tags?: ITag[] | null,
    public orders?: IOrder[] | null
  ) {}
}
