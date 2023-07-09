import { Component, Input, ViewChild, ViewContainerRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FaIconLibrary, FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faCirclePlus } from '@fortawesome/free-solid-svg-icons';
import { File as DonetopFile } from 'src/app/store/model/file.model';
import { FileComponent } from './file/file.component';

@Component({
  selector: 'app-files',
  standalone: true,
  imports: [
    CommonModule,
    FontAwesomeModule
  ],
  templateUrl: './files.component.html',
  styleUrls: ['./files.component.scss']
})
export class FilesComponent {

  @Input() maxSize: number = 5;
  @Input() accept: string = '*/*';
  @ViewChild('fileContainer', { read: ViewContainerRef }) fileContainer!: ViewContainerRef;
  fileComponents: Array<FileComponent> = [];

  constructor(
    private library: FaIconLibrary,
  ) {
    this.library.addIcons(faCirclePlus);
  }

  @Input()
  set files(files: Array<DonetopFile>) {
    files.forEach(file => this.addFileComponent(file));
  }

  existFiles(): Array<File> {
    return this.fileComponents
      .map(fileComponent => fileComponent.fileRef.nativeElement.files)
      .filter(files => files.length > 0)
      .map(files => files[0]);
  }

  reset() {
    this.fileComponents = [];
    this.fileContainer.clear();
  }

  protected addFileComponent(file: DonetopFile | undefined) {
    const fileComponent = this.fileContainer.createComponent(FileComponent).instance;
    fileComponent.file = file;
    fileComponent.accept = this.accept;
    fileComponent.deleteEvent.subscribe(c => this.removeFileComponent(c));
    this.fileComponents.push(fileComponent);
  }

  private removeFileComponent(fileComponent: FileComponent) {
    const index = this.fileComponents.indexOf(fileComponent);
    if (index !== -1) {
      this.fileComponents.splice(index, 1);
      this.fileContainer.remove(index);
    }
  }

}
