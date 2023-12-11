export class SearchCondition {
  constructor(public key: string, public name: string) {}
  static of(key: string, name: string) {
    return new SearchCondition(key, name);
  }
}
