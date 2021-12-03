import { IConnection } from '@/shared/model/connection.model';
import { IProduct } from '@/shared/model/product.model';

export interface ISeller {
  id?: number;
  socialReason?: string;
  address?: string;
  siretNumber?: string | null;
  phone?: number | null;
  email?: string;
  connection?: IConnection | null;
  products?: IProduct[] | null;
}

export class Seller implements ISeller {
  constructor(
    public id?: number,
    public socialReason?: string,
    public address?: string,
    public siretNumber?: string | null,
    public phone?: number | null,
    public email?: string,
    public connection?: IConnection | null,
    public products?: IProduct[] | null
  ) {}
}
