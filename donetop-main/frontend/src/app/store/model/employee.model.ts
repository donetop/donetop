export class Employee {
  constructor(public name: string, public position: string) {}

  static of(name: string, position: string) {
    return new Employee(name, position);
  }

  toString() {
    return this.name + this.position;
  }
}

export const employees: Employee[] = [
  Employee.of('장미연', '팀장'),
  Employee.of('이수정', '과장'),
  Employee.of('김시영', '과장'),
]
