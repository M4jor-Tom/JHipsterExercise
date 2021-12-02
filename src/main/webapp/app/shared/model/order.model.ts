import { IProduct } from '@/shared/model/product.model';
import { IClient } from '@/shared/model/client.model';

import { BillingMethod } from '@/shared/model/enumerations/billing-method.model';
import { OrderState } from '@/shared/model/enumerations/order-state.model';
export interface IOrder {
  id?: number;
  sum?: number | null;
  deliveyAdress?: string;
  deliveryDateTime?: Date;
  quantity?: number;
  billingMethod?: BillingMethod;
  orderState?: OrderState;
  products?: IProduct[] | null;
  client?: IClient | null;
}

export class Order implements IOrder {
  constructor(
    public id?: number,
    public sum?: number | null,
    public deliveyAdress?: string,
    public deliveryDateTime?: Date,
    public quantity?: number,
    public billingMethod?: BillingMethod,
    public orderState?: OrderState,
    public products?: IProduct[] | null,
    public client?: IClient | null
  ) {}
}
