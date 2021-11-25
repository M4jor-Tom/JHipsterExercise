export interface IBrand {
  id?: number;
  name?: string;
}

export class Brand implements IBrand {
  constructor(public id?: number, public name?: string) {}
}
