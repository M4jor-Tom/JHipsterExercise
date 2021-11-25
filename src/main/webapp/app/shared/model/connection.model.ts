export interface IConnection {
  id?: number;
  username?: string | null;
  password?: string | null;
}

export class Connection implements IConnection {
  constructor(public id?: number, public username?: string | null, public password?: string | null) {}
}
