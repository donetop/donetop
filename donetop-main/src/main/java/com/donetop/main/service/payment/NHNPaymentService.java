package com.donetop.main.service.payment;

import com.donetop.enums.payment.Detail.NHNPaymentDetail;
import com.donetop.main.api.nhn.request.PaymentRequest;
import com.donetop.main.api.nhn.request.TradeRegisterRequest;
import com.donetop.main.api.nhn.response.TradeRegisterResponse;

public interface NHNPaymentService {

	NHNPaymentDetail payment(PaymentRequest request) throws Exception;

	TradeRegisterResponse tradeRegister(TradeRegisterRequest request) throws Exception;

}
