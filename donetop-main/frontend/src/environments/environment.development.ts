import { NoOpCryptoService } from "src/app/service/crypto.service";

export const environment = {
  production: false,
  cryptoService: NoOpCryptoService,
  nhn: {
    Ret_URL: `http://${location.hostname}:8080/view/nhn/page/return`
  }
};
