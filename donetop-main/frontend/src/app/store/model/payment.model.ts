export interface PaymentInfo {
  id: number
  paymentStatus: string
  histories: Array<PaymentHistory>
  lastHistory: PaymentHistory
}

export interface PaymentHistory {
  id: number
  pgType: string
  createTime: Date
  detail: Detail
}

interface Detail {
  [key: string]: any;
}
