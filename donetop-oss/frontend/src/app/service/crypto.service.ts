import * as CryptoJS from "crypto-js";

export abstract class CryptoService {
  abstract encrypt(data: string): string;
}

export class NoOpCryptoService extends CryptoService {
  encrypt(data: string): string {
    return data;
  }
}

export class SHA256CryptoService extends CryptoService {
  encrypt(data: string): string {
    return CryptoJS.SHA256(data).toString(CryptoJS.enc.Base64url);
  }
}

