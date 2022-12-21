module.exports = [
    //模版管理
    {
        path:'/modelManagement/modelManagementComponent',
        name:'modelManagementComponent',
        component:() => import('@/views/modelManagement/modelManagementComponent'),
        meta:{
            title: '模版管理',
        }
    },
    {
        path:'/modelManagement/modelAdd',
        name:'modelAdd',
        component:() => import('@/views/modelManagement/modelAdd'),
        meta:{
            title: '模版添加',
        }
    },
]
