import { Folder } from "./folder.model"

export interface Notice {
  id: number
  title: string
  createTime: Date
  folder: Folder
}
