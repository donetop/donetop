import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { firstValueFrom } from "rxjs";
import { Category } from "../store/model/category.model";
import { Response } from "../store/model/response";

@Injectable({
  providedIn: 'root'
})
export class CategoryService {

  private categoriesURI: string = '/api/categories';
  private _categoryArray: Array<Category> | undefined;

  constructor(private httpClient: HttpClient) {}

  async categoryArray() {
    if (this._categoryArray !== undefined) return this._categoryArray;
    this._categoryArray = (await firstValueFrom(this.httpClient.get<Response<Array<Category>>>(this.categoriesURI))).data;
    return this._categoryArray;
  }

}
