import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'stringAbbreviation',
  standalone: true
})
export class StringAbbreviationPipe implements PipeTransform {

  transform(str: string, ...args: any[]) {
    const maxLength = args[0] === undefined ? 10 : args[0];
    const lastDotIndex = str.lastIndexOf('.');
    let name = str.substring(0, lastDotIndex);
    const extension = str.substring(lastDotIndex);
    const hyphens = "---";
    name = name.length > maxLength + hyphens.length ? name.substring(0, maxLength) + hyphens : name;
    return name + extension;
  }

}
