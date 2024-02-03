import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { File } from "../store/model/file.model";
import { Response } from "../store/model/response";

@Injectable({
  providedIn: 'root'
})
export class FileService {

  private filesURI: string = '/api/files';
  private filesSortURI: string = `${this.filesURI}/sort`;

  constructor(private httpClient: HttpClient) {}

  sort(files: Array<File>) {
    return this.httpClient.put<Response<Array<File>>>(this.filesSortURI, { files });
  }

}
