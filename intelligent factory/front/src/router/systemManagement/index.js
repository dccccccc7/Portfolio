module.exports = [
    //田方源 系统管理
    {
        path: '/system/companyManagement',
        name: 'companyManagement',
        component: () => import('@/views/systemManagement/CompanyManagement'),
        meta: {
            title: '公司管理-系统管理',
        }
    },
    {
        path: '/system/userManagement',
        name: 'userManagement',
        component: () => import('@/views/systemManagement/UserManagement'),
        meta: {
            title: '用户管理-系统管理',
        }
    },
    {
        path: '/system/roleManagement',
        name: 'roleManagement',
        component: () => import('@/views/systemManagement/RoleManagement'),
        meta: {
            title: '角色管理-系统管理',
        }
    },
    {
        path: '/system/departmentManagement',
        name: 'departmentManagement',
        component: () => import('@/views/systemManagement/DepartmentManagement'),
        meta: {
            title: '部门管理-系统管理',
        }
    },
    {
        path: '/system/permissionManagement',
        name: 'permissionManagement',
        component: () => import('@/views/systemManagement/PermissionManagement'),
        meta: {
            title: '权限管理-系统管理',
        }
    },{
        path: '/system/userApply',
        name: 'userApply',
        component: () => import('@/views/systemManagement/UserApply'),
        meta: {
            title: '人员审批-系统管理',
        }
    },
]
