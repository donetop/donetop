export interface OSSUser {
  id: number
  name: string
  email: string
  roleType: string
  createTime: string
}

interface RoleTypeCheck {
  (user: OSSUser | undefined): boolean;
}

export const isAdmin: RoleTypeCheck = ossUser => {
  if (!ossUser) return false;
  return ossUser.roleType === 'ADMIN';
};
