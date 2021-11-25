import { IConnection } from '@/shared/model/connection.model';
import { IOrder } from '@/shared/model/order.model';

export interface IClient {
  id?: number;
  addedDateTime?: Date;
  lastName?: string;
  firstName?: string;
  email?: string | null;
  phone?: string | null;
  adress?: string | null;
  country?: string;
  postalCode?: string;
  connection?: IConnection | null;
  orders?: IOrder[] | null;
}

export class Client implements IClient {
  constructor(
    public id?: number,
    public addedDateTime?: Date,
    public lastName?: string,
    public firstName?: string,
    public email?: string | null,
    public phone?: string | null,
    public adress?: string | null,
    public country?: string,
    public postalCode?: string,
    public connection?: IConnection | null,
    public orders?: IOrder[] | null
  ) {}
}
