import { Folder } from "./folder.model";

export interface Comment {
  id: number
  content: string
  createTime: Date
  folder: Folder | undefined
}
