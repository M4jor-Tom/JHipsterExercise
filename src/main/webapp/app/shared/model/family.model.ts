export interface IFamily {
  id?: number;
  name?: string;
}

export class Family implements IFamily {
  constructor(public id?: number, public name?: string) {}
}
