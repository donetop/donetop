import { User } from "../model/user.model";
import { UserAction, UserActionType } from "../action/user.action";

const initialState: User | undefined = undefined;

export function userReducer(state: User | undefined = initialState, action: UserAction) {
  switch (action.type) {
    case UserActionType.LOGIN:
      return action.user;
    case UserActionType.LOGOUT:
      return undefined;
    default:
      return state;
  }
}
