export interface CustomerPost {
  id: number
  customerName: string
  title: string
  content: string
  createTime: Date
  customerPostComments: Array<CustomerPostComment>
  viewCount: number
}

export interface CustomerPostComment {
  id: number
  content: string
  createTime: Date
}
