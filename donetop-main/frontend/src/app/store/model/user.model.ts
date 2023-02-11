export interface User {
  id: number
  name: string
  email: string
  roleType: string
  createTime: string
}

interface RoleTypeCheck {
  (user: User | undefined): boolean;
}

export const isAdmin: RoleTypeCheck = user => {
  if (!user) return false;
  return user.roleType === 'ADMIN';
};
