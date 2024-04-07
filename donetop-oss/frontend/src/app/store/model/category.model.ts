import { Folder } from "./folder.model"

export interface Category {
  id: number
  name: string
  sequence: number
  exposed: boolean
  subCategories: Array<Category>
  folder: Folder | undefined
}
