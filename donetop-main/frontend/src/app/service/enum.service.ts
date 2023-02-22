import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { firstValueFrom } from "rxjs";
import { Category, Enum } from "../store/model/category.model";
import { Response } from "../store/model/response";

@Injectable({
  providedIn: 'root'
})
export class EnumService {

  private enumUri: string = '/api/enum';
  private _categoryArray: Array<Category> | undefined;
  private _paymentMethodArray: Array<Enum> | undefined;
  private _draftStatusArray: Array<Enum> | undefined;

  constructor(private httpClient: HttpClient) {}

  async categoryArray() {
    if (this._categoryArray !== undefined) return this._categoryArray;
    this._categoryArray = (await firstValueFrom(this.httpClient.get<Response<Category[]>>(`${this.enumUri}/category`))).data;
    return this._categoryArray;
  }

  async paymentMethodArray() {
    if (this._paymentMethodArray !== undefined) return this._paymentMethodArray;
    this._paymentMethodArray = (await firstValueFrom(this.httpClient.get<Response<Enum[]>>(`${this.enumUri}/paymentMethod`))).data;
    return this._paymentMethodArray;
  }

  async draftStatusArray() {
    if (this._draftStatusArray !== undefined) return this._draftStatusArray;
    this._draftStatusArray = (await firstValueFrom(this.httpClient.get<Response<Enum[]>>(`${this.enumUri}/draftStatus`))).data;
    return this._draftStatusArray;
  }

}
