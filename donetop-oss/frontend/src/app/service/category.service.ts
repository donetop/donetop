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
  private categoryImageURI: string = `${this.categoryURI}/image`;
  private categoryExposureURI: string = `${this.categoryURI}/{id}/exposure`;
  private categoriesURI: string = '/api/categories';
  private categoriesSortURI: string = `${this.categoriesURI}/sort`;

  constructor(private httpClient: HttpClient) {}

  async categoryArray() {
    return (await firstValueFrom(this.httpClient.get<Response<Array<Category>>>(this.categoriesURI))).data;
  }

  async subCategoryArray(id: number) {
    return (await firstValueFrom(this.httpClient.get<Response<Category>>(`${this.categoryURI}/${id}`))).data.subCategories;
  }

  async get(id: number) {
    return (await firstValueFrom(this.httpClient.get<Response<Category>>(`${this.categoryURI}/${id}`))).data;
  }

  addImage(id: number, formData: FormData) {
    return this.httpClient.post<Response<number>>(`${this.categoryImageURI}/${id}`, formData);
  }

  deleteImage(categoryId: number, fileId: number) {
    return this.httpClient.put<Response<number>>(`${this.categoryImageURI}/${categoryId}`, { fileId });
  }

  createNewCategory(data: object) {
    return this.httpClient.post<Response<number>>(this.categoryURI, data);
  }

  delete(id: number) {
    return this.httpClient.delete<Response<number>>(`${this.categoryURI}/${id}`);
  }

  sort(categories: Array<Category>) {
    return this.httpClient.put<Response<Array<Category>>>(this.categoriesSortURI, { categories });
  }

  toggleExposure(id: number) {
    return this.httpClient.put<Response<void>>(this.categoryExposureURI.replace("{id}", id.toString()), {});
  }

}
