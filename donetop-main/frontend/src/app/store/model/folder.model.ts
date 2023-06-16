import { File } from "./file.model"

export interface Folder {
  id: number
  path: string
  files: Array<File>
}

export interface DraftFolder extends Folder {
  folderType: FolderType
}

export enum FolderType {
  DRAFT_ORDER = "DRAFT_ORDER",
  DRAFT_WORK = "DRAFT_WORK"
}
