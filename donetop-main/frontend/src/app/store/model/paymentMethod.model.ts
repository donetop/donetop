export class PaymentMethod {
  constructor(public en: string, public ko: string) {}

  static of(en: string, ko: string) {
    return new PaymentMethod(en, ko);
  }
}

export const paymentMethods: PaymentMethod[] = [
  PaymentMethod.of('CREDIT_CARD', '신용카드'),
  PaymentMethod.of('CASH', '현금')
]
