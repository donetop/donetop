export interface Enum {
  name: string
  value: string
}

export interface Item {
  value: string
}

export interface Category extends Enum {
  items: Item[]
}
