import { Enum } from "./enum.model"
import { Folder } from "./folder.model"
import { PaymentInfo } from "./payment.model"

export interface Draft {
  id: number
  customerName: string
  companyName: string
  inChargeName: string
  email: string
  phoneNumber: string
  categoryName: string
  draftStatus: Enum
  address: string
  price: number
  paymentMethod: Enum
  memo: string
  createTime: Date
  updateTime: Date
  folder: Folder | undefined
  paymentInfo: PaymentInfo | undefined
}
