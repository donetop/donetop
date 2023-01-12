export class Category {
  constructor(public name: string, public items: Item[]) {}

  static of(name: string, items: Item[]) {
    return new Category(name, items);
  }
}

export class Item {
  constructor(public name: string) {}

  static of(name: string): Item {
    return new Item(name);
  }
}

export const categories: Category[] = [
  Category.of('현수막', [
    Item.of('선거전문현수막'), Item.of('게릴라현수막'), Item.of('법정게시대'),
    Item.of('족자현수막'), Item.of('차량용현수막'), Item.of('유포지(썬팅용)'),
    Item.of('소형현수막'), Item.of('깃발/어깨띠')
  ]),
  Category.of('배너', [
    Item.of('현수막배너'), Item.of('펫트배너'), Item.of('미니배너'),
    Item.of('대형배너')
  ]),
  Category.of('유포지실사', []),
  Category.of('전단지', [
    Item.of('단면/양면전단'), Item.of('소량전단'), Item.of('문어발전단'),
    Item.of('문고리전단'), Item.of('전단지')
  ]),
  Category.of('명함', [
    Item.of('일반지명함'), Item.of('수입지명함'), Item.of('카드명함'),
    Item.of('쿠폰명함')
  ]),
  Category.of('스티커/자석', [
    Item.of('비규격스티커'), Item.of('규격스티커'), Item.of('모양스티커(규격)'),
    Item.of('모양스티커(비규격)'), Item.of('종이자석'), Item.of('차량자석')
  ]),
  Category.of('카달로그', [
    Item.of('음식점'), Item.of('교회'), Item.of('은행'),
    Item.of('유치원'), Item.of('스포츠'), Item.of('학원'),
    Item.of('판매'), Item.of('학교'), Item.of('회사'),
    Item.of('선거홍보물')
  ]),
  Category.of('봉투', [
    Item.of('컬러(대/중/소)'), Item.of('흑백(대/중/소)')
  ]),
  Category.of('양식ㆍ서식지/영수증', [
    Item.of('양식/서식(낱장)'), Item.of('양식/서식(제본)'), Item.of('NCR지(2조/3조)'),
    Item.of('영수증')
  ]),
  Category.of('아크릴/포멕스 간판', [
    Item.of('아크릴'), Item.of('포멕스'), Item.of('표찰/명찰')
  ])
];
