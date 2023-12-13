const prefix = 'view';

export class RouteName {

  public readonly ROOT = ``;

  public readonly HOME = `${prefix}/home`;

  public readonly LOGIN = `${prefix}/login`;

  public readonly DRAFT = `${prefix}/draft`;

  public readonly DRAFT_CHILDREN = {
    CREATE: `create`, LIST: `list`, DETAIL: `detail`, UPDATE: `update`
  }

  public readonly DRAFT_CREATE = `${this.DRAFT}/${this.DRAFT_CHILDREN.CREATE}`;

  public readonly DRAFT_LIST = `${this.DRAFT}/${this.DRAFT_CHILDREN.LIST}`;

  public readonly DRAFT_DETAIL = `${this.DRAFT}/${this.DRAFT_CHILDREN.DETAIL}`;

  public readonly DRAFT_UPDATE = `${this.DRAFT}/${this.DRAFT_CHILDREN.UPDATE}`;

  public readonly CUSTOMERPOST = `${prefix}/customerpost`;

  public readonly CUSTOMERPOST_CHILDREN = {
    CREATE: `create`, LIST: `list`, DETAIL: `detail`
  }

  public readonly CUSTOMERPOST_CREATE = `${this.CUSTOMERPOST}/${this.CUSTOMERPOST_CHILDREN.CREATE}`;

  public readonly CUSTOMERPOST_LIST = `${this.CUSTOMERPOST}/${this.CUSTOMERPOST_CHILDREN.LIST}`;

  public readonly CUSTOMERPOST_DETAIL = `${this.CUSTOMERPOST}/${this.CUSTOMERPOST_CHILDREN.DETAIL}`;

  public readonly CATEGORY = `${prefix}/category`;

  public readonly INTRODUCTION = `${prefix}/introduction`;

  public readonly TERMS = `${prefix}/terms`;

  public readonly POLICY = `${prefix}/policy`;

  public readonly USAGE = `${prefix}/usage`;

  public readonly FALL_BACK = `**`;

  private constructor() {}

  public static INSTANCE = new RouteName();
}
