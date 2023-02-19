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

  public readonly FALL_BACK = `**`;

  private constructor() {}

  public static INSTANCE = new RouteName();
}
