import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Response } from '../store/model/response';
import { Page } from '../store/model/page.model';
import { Draft } from '../store/model/draft.model';
import { firstValueFrom } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DraftService {

  private draftURI: string = '/api/draft';
  private draftCopyURI: string = this.draftURI + '/copy';
  private draftsURI: string = '/api/drafts';
  private draftPartialURI: string = this.draftURI + "/partial";
  private draftCommentCheckURI: string = this.draftURI + "/comment/check";

  constructor(private httpClient: HttpClient) {}

  create(formData: FormData) {
    return this.httpClient.post<Response<number>>(this.draftURI, formData);
  }

  update(id: number, formData: FormData) {
    return this.httpClient.put<Response<number>>(`${this.draftURI}/${id}`, formData);
  }

  updatePartial(id: number, body: object) {
    return this.httpClient.put<Response<number>>(`${this.draftPartialURI}/${id}`, body);
  }

  list(params: object) {
    let requestURI = this.draftsURI;
    let first = true;
    for (const [key, value] of Object.entries(params)) {
      const symbol = first ? '?' : '&';
      requestURI += `${symbol}${key}=${value}`;
      if (first) first = false;
    }
    return this.httpClient.get<Response<Page<Draft>>>(requestURI);
  }

  get(id: number, password: string) {
    return this.httpClient.get<Response<Draft>>(`${this.draftURI}/${id}?password=${password}`);
  }

  delete(id: number) {
    return this.httpClient.delete<Response<number>>(`${this.draftURI}/${id}`);
  }

  deleteMultiple(draftIds: Array<Number>) {
    return this.httpClient.put<Response<number>>(this.draftsURI, { draftIds });
  }

  copy(id: number) {
    return this.httpClient.post<Response<number>>(this.draftCopyURI, { id });
  }

  async checkComments(id: number) {
    return (await firstValueFrom(this.httpClient.put<Response<number>>(`${this.draftCommentCheckURI}/${id}`, {}))).data;
  }

}
