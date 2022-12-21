module.exports = [
    {
        path: '/process/production/add',
        name: '生产过程管理-产品',
        component: () => import('@/views/production_process/production/Add'),
        meta: {
            title: '添加-产品-生产过程管理'
        },
        children: [
            {
                path: '',
                redirect: 'product'
            },
            {
                path: 'product',
                component: () => import('@/views/production_process/production/AddProduct'),
                meta: {
                    title: '添加-产品-生产过程管理'
                }
            },
            {
                path: 'process',
                component: () => import('@/views/production_process/production/AddProcess'),
                meta: {
                    title: '添加-产品-生产过程管理'
                }
            },
            {
                path: 'material',
                component: () => import('@/views/production_process/production/AddMaterial'),
                meta: {
                    title: '添加-产品-生产过程管理'
                }
            },
            {
                path: 'ensure',
                component: () => import('@/views/production_process/production/AddEnsure'),
                meta: {
                    title: '添加-产品-生产过程管理'
                }
            },
        ]
    },
    {
        path: '/process/production/list',
        name: '生产过程管理-查看',
        component: () => import('@/views/production_process/production/List'),
        meta: {
            title: '查看-产品-生产过程管理'
        }
    },
    {
        path: '/process/production/detail',
        name: '生产过程管理-查看',
        component: () => import('@/views/production_process/production/Detail'),
        meta: {
            title: '查看-产品-生产过程管理'
        }
    }
]