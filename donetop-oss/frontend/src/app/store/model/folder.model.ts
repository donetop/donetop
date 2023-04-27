import { File } from "./file.model"

export interface Folder {
  id: number
  path: string
  files: Array<File>
}
