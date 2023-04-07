export interface IFridge {
  id: number;
  name?: string | null;
  location?: string | null;
}

export type NewFridge = Omit<IFridge, 'id'> & { id: null };
