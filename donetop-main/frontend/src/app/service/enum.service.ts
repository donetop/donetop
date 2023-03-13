import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { firstValueFrom } from "rxjs";
import { Enum } from "../store/model/enum.model";
import { Response } from "../store/model/response";

@Injectable({
  providedIn: 'root'
})
export class EnumService {

  private enumURI: string = '/api/enum';
  private _paymentMethodArray: Array<Enum> | undefined;
  private _draftStatusArray: Array<Enum> | undefined;

  constructor(private httpClient: HttpClient) {}

  async paymentMethodArray() {
    if (this._paymentMethodArray !== undefined) return this._paymentMethodArray;
    this._paymentMethodArray = (await firstValueFrom(this.httpClient.get<Response<Array<Enum>>>(`${this.enumURI}/paymentMethod`))).data;
    return this._paymentMethodArray;
  }

  async draftStatusArray() {
    if (this._draftStatusArray !== undefined) return this._draftStatusArray;
    this._draftStatusArray = (await firstValueFrom(this.httpClient.get<Response<Array<Enum>>>(`${this.enumURI}/draftStatus`))).data;
    return this._draftStatusArray;
  }

}
