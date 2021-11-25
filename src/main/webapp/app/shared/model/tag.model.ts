import { IProduct } from '@/shared/model/product.model';

export interface ITag {
  id?: number;
  name?: string;
  products?: IProduct[] | null;
}

export class Tag implements ITag {
  constructor(public id?: number, public name?: string, public products?: IProduct[] | null) {}
}
