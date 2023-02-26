import { Draft } from "./draft.model"

export interface OrderRequestParameter {
  site_cd: string
  ordr_idxx: string
  pay_method: string
  good_name: string
  good_mny: string
  site_name: string
  buyr_name: string
  buyr_mail: string
}

interface OrderRequestParameterFrom<T> {
  (t: T): OrderRequestParameter;
}

export const OrderRequestParameterFrom: OrderRequestParameterFrom<Draft> = draft => {
  return {
    'site_cd': 'T0000',
    'ordr_idxx': `DRAFT-${draft.id}-TEST`,
    'pay_method': '100000000000',
    'good_name': `디원탑 시안(${draft.id})`,
    'good_mny': `${draft.price}`,
    'site_name': 'DONETOP',
    'buyr_name': draft.customerName,
    'buyr_mail': draft.email
  }
}
