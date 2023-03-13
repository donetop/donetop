import { Folder } from "./folder.model"

export interface Category {
  id: number
  name: string
  sequence: number
  subCategories: Array<Category>
  folder: Folder | undefined
}
