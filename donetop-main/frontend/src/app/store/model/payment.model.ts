export interface PaymentInfo {
  id: number
  updateTime: Date
  lastTransactionNumber: string
  histories: Array<PaymentHistory>
  lastHistory: PaymentHistory
}

export interface PaymentHistory {
  id: number
  pgType: string
  paymentStatus: string
  createTime: Date
  detail: Detail
}

interface Detail {
  [key: string]: any;
}
