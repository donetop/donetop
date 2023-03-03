async function trade_register(tradeRegisterRequest) {
  const response = await fetch('api/nhn/trade/register', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(tradeRegisterRequest)
  });
  return await response.json();
}

function call_pay_form() {
  const mobile_order_form = document.mobile_order_form;
  const PayUrl = mobile_order_form.PayUrl.value;
  const encoding = mobile_order_form.encoding_trans.value;
  mobile_order_form.action = encoding === 'UTF-8' ? PayUrl.substring(0, PayUrl.lastIndexOf("/"))  + "/jsp/encodingFilter/encodingFilter.jsp" : PayUrl;

  if (mobile_order_form.Ret_URL.value === "") {
    alert("연동시 Ret_URL을 반드시 설정하셔야 됩니다.");
    return false;
  } else {
    alert(mobile_order_form.Ret_URL.value);
    mobile_order_form.submit();
  }
}
