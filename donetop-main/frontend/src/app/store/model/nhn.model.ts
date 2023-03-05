import { environment } from "src/environments/environment"
import { Draft } from "./draft.model"

export interface OrderRequest {
  site_cd: string
  ordr_idxx: string
  pay_method: string
  good_name: string
  good_mny: string
  site_name: string
  buyr_name: string
  buyr_mail: string
}

export interface TradeRegisterRequest {
  site_cd: string
  ordr_idxx: string
  good_mny: string
  pay_method: string
  good_name: string
  Ret_URL: string
  escw_used: string
  user_agent: string
}

interface OrderRequestFrom<T> {
  (t: T): OrderRequest;
}

interface TradeRegisterFrom<T> {
  (t: T): TradeRegisterRequest
}

export const OrderRequestFromDraft: OrderRequestFrom<Draft> = draft => {
  return {
    'site_cd': 'T0000',
    'ordr_idxx': `DRAFT-${draft.id}-TEST`,
    'pay_method': '100000000000',
    'good_name': `DONETOP DRAFT(${draft.id})`,
    'good_mny': `${draft.price}`,
    'site_name': 'DONETOP',
    'buyr_name': draft.customerName,
    'buyr_mail': draft.email
  }
}

export const TradeRegisterRequestFromDraft: TradeRegisterFrom<Draft> = draft => {
  return {
    'site_cd': 'T0000',
    'ordr_idxx': `DRAFT-${draft.id}-TEST`,
    'good_mny': `${draft.price}`,
    'pay_method': 'CARD',
    'good_name': `DONETOP DRAFT(${draft.id})`,
    'Ret_URL': environment.nhn.Ret_URL,
    'escw_used': 'N',
    'user_agent': window.navigator.userAgent
  }
}
