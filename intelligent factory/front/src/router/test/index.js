module.exports = [
    {
        path: '/welcome',
        name: 'testComponent1',
        component: () => import('@/views/test/TestComponent1'),
        meta: {
            title: '欢迎使用车间管理系统！',
        }
    },
    {
        path: '/test/testComponent2',
        name: 'testComponent2',
        component: () => import('@/views/test/TestComponent2'),
        meta: {
            title: '测试组件2-测试组件',
        }
    },
]
