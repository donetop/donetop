const prefix = 'view';

export class RouteName {

  public readonly ROOT = ``;

  public readonly HOME = `${prefix}/home`;

  public readonly LOGIN = `${prefix}/login`;

  public readonly CATEGORY = `${prefix}/category`;

  public readonly CATEGORY_CHILDREN = {
    LIST: `list`, IMAGE: `image`
  };

  public readonly CATEGORY_LIST = `${this.CATEGORY}/${this.CATEGORY_CHILDREN.LIST}`;

  public readonly CATEGORY_IMAGE = `${this.CATEGORY}/${this.CATEGORY_CHILDREN.IMAGE}`;

  public readonly PAYMENT = `${prefix}/payment`;

  public readonly PAYMENT_CHILDREN = {
    LIST: `list`
  }

  public readonly PAYMENT_LIST = `${this.PAYMENT}/${this.PAYMENT_CHILDREN.LIST}`;

  public readonly FALL_BACK = `**`;

  private constructor() {}

  public static INSTANCE = new RouteName();
}
