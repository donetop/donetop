import { Folder } from "./folder.model"

export interface Draft {
  id: number
  customerName: string
  companyName: string
  inChargeName: string
  email: string
  phoneNumber: string
  category: string
  draftStatus: string
  address: string
  price: number
  paymentMethod: string
  memo: string
  createTime: Date
  updateTime: Date
  folder: Folder | undefined
}
