export interface Page<T> {
  content: Array<T>
  pageable: object
  last: boolean
  totalPages: number
  totalElements: number
  first: boolean
  numberOfElements: number
  size: number
  number: number
  sort: object
  empty: boolean
}
