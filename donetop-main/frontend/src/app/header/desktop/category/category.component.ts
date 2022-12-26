import { Component, ElementRef, ViewChild, HostListener } from '@angular/core';

@Component({
  selector: 'app-category',
  templateUrl: './category.component.html',
  styleUrls: ['./category.component.scss']
})
export class CategoryComponent {

  placard: Group = Group.of('현수막', [
    Item.of('선거전문현수막'), Item.of('게릴라현수막'), Item.of('법정게시대'),
    Item.of('족자현수막'), Item.of('차량용현수막'), Item.of('유포지(썬팅용)'),
    Item.of('소형현수막'), Item.of('깃발/어깨띠')
  ]);

  banner: Group = Group.of('배너', [
    Item.of('현수막배너'), Item.of('펫트배너'), Item.of('미니배너'),
    Item.of('대형배너')
  ]);

  eupoji: Group = Group.of('유포지실사', []);

  leaflet: Group = Group.of('전단지', [
    Item.of('단면/양면전단'), Item.of('소량전단'), Item.of('문어발전단'),
    Item.of('문고리전단'), Item.of('전단지')
  ]);

  card: Group = Group.of('명함', [
    Item.of('일반지명함'), Item.of('수입지명함'), Item.of('카드명함'),
    Item.of('쿠폰명함')
  ]);

  sticker_magnet: Group = Group.of('스티커/자석', [
    Item.of('비규격스티커'), Item.of('규격스티커'), Item.of('모양스티커(규격)'),
    Item.of('모양스티커(비규격)'), Item.of('종이자석'), Item.of('차량자석')
  ]);

  catalog: Group = Group.of('카달로그', [
    Item.of('음식점'), Item.of('교회'), Item.of('은행'),
    Item.of('유치원'), Item.of('스포츠'), Item.of('학원'),
    Item.of('판매'), Item.of('학교'), Item.of('회사'),
    Item.of('선거홍보물')
  ]);

  envelope: Group = Group.of('봉투', [
    Item.of('컬러(대/중/소)'), Item.of('흑백(대/중/소)')
  ]);

  form_format_receipt: Group = Group.of('양식ㆍ서식지/영수증', [
    Item.of('양식/서식(낱장)'), Item.of('양식/서식(제본)'), Item.of('NCR지(2조/3조)'),
    Item.of('영수증')
  ]);

  acryl_pomax_sign: Group = Group.of('아크릴/포멕스 간판', [
    Item.of('아크릴'), Item.of('포멕스'), Item.of('표찰/명찰')
  ]);

  showCategory: boolean = false;
  @ViewChild('category_open_button', { static: true }) categoryOpenButton!: ElementRef;
  @ViewChild('category_close_button', { static: true }) categoryCloseButton!: ElementRef;
  @ViewChild('category', { static: true }) category!: ElementRef;

  constructor(private eRef: ElementRef) {}

  toggleCategory() {
    this.showCategory = !this.showCategory;
    this.categoryOpenButton.nativeElement.style.visibility = this.showCategory ? 'hidden' : 'visible';
    this.categoryCloseButton.nativeElement.style.visibility = this.showCategory ? 'visible' : 'hidden';
    this.category.nativeElement.style.visibility = this.showCategory ? 'visible' : 'hidden';
  }

  @HostListener('document:click', ['$event'])
  click(event: PointerEvent) {
    if (this.eRef.nativeElement.contains(event.target)) {
      if (!this.showCategory) this.toggleCategory();
      else if (!this.isliTag(event)) this.toggleCategory();
    } else {
      if (this.showCategory) this.toggleCategory();
    }
  }

  private isliTag(event: PointerEvent): boolean {
    return document.elementFromPoint(event.clientX, event.clientY)?.tagName == 'LI';
  }
}

class Group {
  constructor(public name: string, public items: Item[]) {}

  static of(name: string, items: Item[]): Group {
    return new Group(name, items);
  }
}

class Item {
  constructor(public name: string) {}

  static of(name: string): Item {
    return new Item(name);
  }
}
