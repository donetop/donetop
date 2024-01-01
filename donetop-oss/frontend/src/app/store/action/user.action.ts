import { Action } from "@ngrx/store";
import { User } from "../model/user.model";

export enum UserActionType {
  LOAD = '[User] load.',
  UNLOAD = '[User] unload.',
}

export class UserLoadAction implements Action {
  readonly type = UserActionType.LOAD;
  constructor(public user: User) {}
}

export class UserUnloadAction implements Action {
  readonly type = UserActionType.UNLOAD;
}

export type UserAction = UserLoadAction | UserUnloadAction;
