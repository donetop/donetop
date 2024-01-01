import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'zeroPricePipe',
  standalone: true
})
export class ZeroPricePipe implements PipeTransform {

  transform(price: string | null, ...args: any[]) {
    return price === '0' ? '-----' : price;
  }

}
