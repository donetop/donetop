async function m_Completepayment(FormOrJson, closeEvent) {

    const draft_order_form = document.draft_order_form;
    GetField(draft_order_form, FormOrJson);

    if (isValid(draft_order_form)) {
        const response = await proceedPayment(draft_order_form);
        const msg = response.code === 200 ? response.data.res_msg : response.reason;
        alert(msg);
        location.reload();
    } else {
        alert("[" + draft_order_form.res_cd.value + "] " + draft_order_form.res_msg.value);
        closeEvent();
    }

    function isValid(form) {
      if (!form) return false;
      if (!form.res_cd) return false;
      if (form.res_cd.value !== "0000") return false;
      if (!form.ordr_idxx || !form.good_mny || !form.ordr_chk) return false;
      const split = draft_order_form.ordr_chk.value.split('|');
      if (form.ordr_idxx.value !== split[0] || form.good_mny.value !== split[1]) return false;
      return true;
    }

    async function proceedPayment(form) {
      const params = {};
      for (let i = 0; i < form.elements.length; i++) {
        const element = form.elements[i];
        params[element.name] = element.value;
      }
      const response = await fetch(form.action, {
        method: form.method,
        body: new URLSearchParams(params)
      });
      return await response.json();
    }
}
