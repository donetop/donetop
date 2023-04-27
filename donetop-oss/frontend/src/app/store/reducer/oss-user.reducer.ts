import { OSSUser } from "../model/oss-user.model";
import { OSSUserAction, OSSUserActionType } from "../action/oss-user.action";

const initialState: OSSUser | undefined = undefined;

export function ossUserReducer(state: OSSUser | undefined = initialState, action: OSSUserAction) {
  switch (action.type) {
    case OSSUserActionType.LOAD:
      return action.ossUser;
    case OSSUserActionType.UNLOAD:
      return undefined;
    default:
      return state;
  }
}
