module.exports = [
    {
        path: '/process/procedure/add',
        name: '生产过程管理-工序-添加',
        component: () => import('@/views/production_process/procedure/Add'),
        meta: {
            title: '添加-工序-生产过程管理'
        },
    },
    {
        path: '/process/procedure/list',
        name: '生产过程管理-工序-查看',
        component: () => import('@/views/production_process/procedure/List'),
        meta: {
            title: '查看-工序-生产过程管理'
        }
    }
]