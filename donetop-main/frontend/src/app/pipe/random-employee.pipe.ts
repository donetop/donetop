import { Pipe, PipeTransform } from '@angular/core';
import { Employee } from '../store/model/employee.model';

@Pipe({
  name: 'randomEmployee',
  standalone: true
})
export class RandomEmployeePipe implements PipeTransform {

  transform(employees: Employee[], ...args: any[]): Employee {
    return employees[Math.floor(Math.random() * employees.length)];
  }

}
