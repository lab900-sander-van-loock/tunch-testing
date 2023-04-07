export interface IBeer {
  id: number;
  name?: string | null;
  brewery?: string | null;
  percentage?: number | null;
}

export type NewBeer = Omit<IBeer, 'id'> & { id: null };
