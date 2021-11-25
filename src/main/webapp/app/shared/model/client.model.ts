import { IConnection } from '@/shared/model/connection.model';
import { IOrder } from '@/shared/model/order.model';

export interface IClient {
  id?: number;
  addedDateTime?: Date | null;
  lastName?: string | null;
  firstName?: string | null;
  email?: string | null;
  phone?: string | null;
  adress?: string | null;
  country?: string | null;
  postalCode?: string | null;
  connection?: IConnection | null;
  orders?: IOrder[] | null;
}

export class Client implements IClient {
  constructor(
    public id?: number,
    public addedDateTime?: Date | null,
    public lastName?: string | null,
    public firstName?: string | null,
    public email?: string | null,
    public phone?: string | null,
    public adress?: string | null,
    public country?: string | null,
    public postalCode?: string | null,
    public connection?: IConnection | null,
    public orders?: IOrder[] | null
  ) {}
}
