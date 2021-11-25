export interface IFamily {
  id?: number;
  name?: string | null;
}

export class Family implements IFamily {
  constructor(public id?: number, public name?: string | null) {}
}
