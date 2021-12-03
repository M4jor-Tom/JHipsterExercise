import { IProduct } from '@/shared/model/product.model';
import { IClient } from '@/shared/model/client.model';

import { BillingMethod } from '@/shared/model/enumerations/billing-method.model';
import { OrderState } from '@/shared/model/enumerations/order-state.model';
export interface IOrder {
  id?: number;
  sum?: number | null;
  deliveryAdress?: string | null;
  deliveryDateTime?: Date | null;
  quantity?: number;
  billingMethod?: BillingMethod | null;
  orderState?: OrderState;
  products?: IProduct[] | null;
  client?: IClient | null;
}

export class Order implements IOrder {
  constructor(
    public id?: number,
    public sum?: number | null,
    public deliveryAdress?: string | null,
    public deliveryDateTime?: Date | null,
    public quantity?: number,
    public billingMethod?: BillingMethod | null,
    public orderState?: OrderState,
    public products?: IProduct[] | null,
    public client?: IClient | null
  ) {}
}
