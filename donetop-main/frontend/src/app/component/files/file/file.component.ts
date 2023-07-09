import { Component, ElementRef, EventEmitter, Input, Output, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FaIconLibrary, FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faCirclePlus, faDownload, faXmark } from '@fortawesome/free-solid-svg-icons';
import { File } from 'src/app/store/model/file.model';

@Component({
  selector: 'app-file',
  standalone: true,
  imports: [
    CommonModule,
    FontAwesomeModule
  ],
  templateUrl: './file.component.html',
  styleUrls: ['./file.component.scss']
})
export class FileComponent {

  @Input() accept: string = '*/*';
  @Output() deleteEvent = new EventEmitter<FileComponent>();
  @ViewChild('file', { static: true }) fileRef!: ElementRef;

  constructor(
    private library: FaIconLibrary
  ) {
    this.library.addIcons(faDownload, faCirclePlus, faXmark);
  }

  @Input()
  set file(file: File | undefined) {
    if (file) this.setFileToFileRef(file);
  }

  private async setFileToFileRef(file: File) {
    const fileInput = this.fileRef.nativeElement;
    const myFile = new File([await this.convertURLtoFile(`/api/file/${file.id}`)], file.name, { type: file.mimeType });
    const dataTransfer = new DataTransfer();
    dataTransfer.items.add(myFile);
    fileInput.files = dataTransfer.files;
  }

  private async convertURLtoFile(url: string) {
    return await (await fetch(url)).blob();
  }

  deleteMe() {
    this.deleteEvent.emit(this);
  }

}
