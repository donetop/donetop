import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { firstValueFrom } from "rxjs";
import { Category } from "../store/model/category.model";
import { Response } from "../store/model/response";

@Injectable({
  providedIn: 'root'
})
export class CategoryService {

  private categoryURI: string = '/api/category';
  private categoriesURI: string = '/api/categories';

  constructor(private httpClient: HttpClient) {}

  async categoryArray() {
    return (await firstValueFrom(this.httpClient.get<Response<Array<Category>>>(this.categoriesURI))).data;
  }

  async get(id: number) {
    return (await firstValueFrom(this.httpClient.get<Response<Category>>(`${this.categoryURI}/${id}`))).data;
  }

}
