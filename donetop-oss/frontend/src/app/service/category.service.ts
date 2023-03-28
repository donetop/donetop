import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Router } from "@angular/router";
import { firstValueFrom } from "rxjs";
import { Category } from "../store/model/category.model";
import { Response } from "../store/model/response";
import { RouteName } from "../store/model/routeName.model";

@Injectable({
  providedIn: 'root'
})
export class CategoryService {

  private routeName = RouteName.INSTANCE;
  private categoryURI: string = '/api/category';
  private categoriesURI: string = '/api/categories';

  constructor(private httpClient: HttpClient, private router: Router) {}

  async categoryArray() {
    return (await firstValueFrom(this.httpClient.get<Response<Array<Category>>>(this.categoriesURI))).data;
  }

  async subCategoryArray(id: number) {
    return (await firstValueFrom(this.httpClient.get<Response<Category>>(`${this.categoryURI}/${id}`))).data.subCategories;
  }

  createNewCategory(data: object) {
    return this.httpClient.post<Response<number>>(this.categoryURI, data);
  }

  delete(id: number) {
    return this.httpClient.delete<Response<number>>(`${this.categoryURI}/${id}`);
  }

  sort(categories: Array<Category>) {
    return this.httpClient.put<Response<Array<Category>>>(`${this.categoriesURI}/sort`, { categories });
  }

}
