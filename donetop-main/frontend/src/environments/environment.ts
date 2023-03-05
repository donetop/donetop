import { SHA256CryptoService } from "src/app/service/crypto.service";

export const environment = {
  production: true,
  cryptoService: SHA256CryptoService,
  nhn: {
    Ret_URL: `${location.origin}/view/nhn/page/return`
  }
};
