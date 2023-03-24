import { Action } from "@ngrx/store";
import { OSSUser } from "../model/oss-user.model";

export enum OSSUserActionType {
  LOAD = '[OSSUser] load.',
  UNLOAD = '[OSSUser] unload.',
}

export class OSSUserLoadAction implements Action {
  readonly type = OSSUserActionType.LOAD;
  constructor(public ossUser: OSSUser) {}
}

export class OSSUserUnloadAction implements Action {
  readonly type = OSSUserActionType.UNLOAD;
}

export type OSSUserAction = OSSUserLoadAction | OSSUserUnloadAction;
