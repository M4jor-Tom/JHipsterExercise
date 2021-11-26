export interface IConnection {
  id?: number;
  username?: string;
  password?: string;
}

export class Connection implements IConnection {
  constructor(public id?: number, public username?: string, public password?: string) {}
}
