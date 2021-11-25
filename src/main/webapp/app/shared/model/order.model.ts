import { IProduct } from '@/shared/model/product.model';
import { IClient } from '@/shared/model/client.model';

import { BillingMethod } from '@/shared/model/enumerations/billing-method.model';
export interface IOrder {
  id?: number;
  sum?: number | null;
  deliveyAdress?: string | null;
  deliveryDateTime?: Date | null;
  billingMethod?: BillingMethod | null;
  products?: IProduct[] | null;
  client?: IClient | null;
}

export class Order implements IOrder {
  constructor(
    public id?: number,
    public sum?: number | null,
    public deliveyAdress?: string | null,
    public deliveryDateTime?: Date | null,
    public billingMethod?: BillingMethod | null,
    public products?: IProduct[] | null,
    public client?: IClient | null
  ) {}
}
