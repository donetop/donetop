function m_Completepayment(FormOrJson, closeEvent) {

    const draft_order_form = document.draft_order_form;

    /********************************************************************/
    /* FormOrJson은 가맹점 임의 활용 금지                               */
    /* frm 값에 FormOrJson 값이 설정 됨 frm 값으로 활용 하셔야 됩니다.  */
    /* FormOrJson 값을 활용 하시려면 기술지원팀으로 문의바랍니다.       */
    /********************************************************************/
    GetField(draft_order_form, FormOrJson);

    if (draft_order_form.res_cd.value == "0000") {
        // alert("결제 승인 요청 전,\n\n반드시 결제창에서 고객님이 결제 인증 완료 후\n\n리턴 받은 ordr_chk 와 업체 측 주문정보를\n\n다시 한번 검증 후 결제 승인 요청하시기 바랍니다."); //업체 연동 시 필수 확인 사항.
        // draft_order_form.submit();
        console.log(draft_order_form);
    } else {
        alert("[" + draft_order_form.res_cd.value + "] " + draft_order_form.res_msg.value);
        closeEvent();
    }
}
