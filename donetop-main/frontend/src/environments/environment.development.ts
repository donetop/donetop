import { NoOpCryptoService } from "src/app/service/crypto.service";

export const environment = {
  production: false,
  cryptoService: NoOpCryptoService
};
