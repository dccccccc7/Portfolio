module.exports = [
    //产品质量管理
    {
        path:'/qualityManagement/qualityTaskListComponent',
        name:'qualityTaskListComponent',
        component:() => import('@/views/qualityManagement/qualityTaskListComponent'),
        meta:{
            title: '任务清单-产品质量管理',
        }
    },
    {
        path:'/qualityManagement/defectManagementComponent',
        name:'defectManagementComponent',
        component:() => import('@/views/qualityManagement/defectManagementComponent'),
        meta:{
            title: '瑕疵管理-产品质量管理',
        }
    },
    {
        path:'/qualityManagement/qualityManagementComponent',
        name:'qualityManagementComponent',
        component:() => import('@/views/qualityManagement/qualityManagementComponent'),
        meta:{
            title: '品质管理-产品质量管理',
        }
    },
    {
        path: '/components/qualityManagement/quality/EditForm',
        name: 'editForm',
        component: () => import('@/components/qualityManagement/EditForm'),
        meta: {
            title: '任务清单-插入质检结果'
        }
    },
    {
        path: '/components/qualityManagement/quality/qualityResults',
        name: 'viewForm',
        component: () => import('@/components/qualityManagement/qualityResultByList'),
        meta: {
            title: '任务清单-查看质检结果'
        }
    },

    {
        path: '/components/qualityManagement/quality/allQualityResults',
        name: 'viewAllForm',
        component: () => import('@/components/qualityManagement/qualityResults'),
        meta: {
            title: '任务清单-查看所有质检结果'
        }
    },
]
