import { Category, Enum } from "./category.model"
import { Folder } from "./folder.model"

export interface Draft {
  id: number
  customerName: string
  companyName: string
  inChargeName: string
  email: string
  phoneNumber: string
  category: Category
  draftStatus: Enum
  address: string
  price: number
  paymentMethod: Enum
  memo: string
  createTime: Date
  updateTime: Date
  folder: Folder | undefined
}
