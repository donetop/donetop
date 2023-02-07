export interface Response<T> {
  status: string,
  code: number,
  data: T
}
