import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'emptyStringPipe',
  standalone: true
})
export class EmptyStringPipe implements PipeTransform {

  transform(str: string | null, ...args: any[]) {
    return str === '' ? 'N/A' : str;
  }

}
