import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'inChargeNamePipe',
  standalone: true
})
export class InChargeNamePipe implements PipeTransform {

  transform(inChargeName: string, ...args: any[]) {
    return inChargeName.length === 0 ? '미정' : inChargeName;
  }

}
