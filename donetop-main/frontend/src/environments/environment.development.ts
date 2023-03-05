import { NoOpCryptoService } from "src/app/service/crypto.service";

export const environment = {
  production: false,
  cryptoService: NoOpCryptoService,
  nhn: {
    site_cd: 'T0000',
    Ret_URL: `http://${location.hostname}:8080/view/nhn/page/return`
  }
};
