import { Action } from "@ngrx/store";
import { User } from "../model/user.model";

export enum UserActionType {
  LOGIN = '[User] login.',
  LOGOUT = '[User] logout.',
}

export class LoginAction implements Action {
  readonly type = UserActionType.LOGIN;
  constructor(public user: User) {}
}

export class LogoutAction implements Action {
  readonly type = UserActionType.LOGOUT;
}

export type UserAction = LoginAction | LogoutAction;
