import { NoOpCryptoService } from "src/app/service/crypto.service";
import { Draft } from "src/app/store/model/draft.model";

export const environment = {
  production: false,
  cryptoService: NoOpCryptoService,
  nhn: {
    site_cd: 'T0000',
    Ret_URL: `http://${location.hostname}:8080/view/nhn/page/return`,
    ordr_idxx(draft: Draft) {
      return `DRAFT-${draft.id}-TEST`;
    }
  },
  timeZone: 'America/Vancouver',
  adminSiteURL: 'http://localhost:8070'
};
