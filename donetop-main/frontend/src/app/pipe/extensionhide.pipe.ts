import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'extensionHidePipe',
  standalone: true
})
export class ExtensionHidePipe implements PipeTransform {

  transform(str: string) {
    const dotIdx = str.indexOf('.');
    if (dotIdx === -1) return str;
    return str.substring(0, dotIdx);
  }

}
