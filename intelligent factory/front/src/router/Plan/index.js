
module.exports = [
  {
      path: '/process/:id',
      name: '生产计划管理-生产计划-生产过程管理',
      component: () => import('@/views/Plan/Process'),
      meta: {
          title: '生产过程管理-生产过程管理-生产计划管理'
      },
  },
  {
    path: '/distribution',
    name: '生产计划管理-客户需求单管理-分配',
    component: () => import('@/views/Plan/Distribution'),
    meta: {
        title: '分配-客户需求单管理-生产计划管理'
    },
},
{
  path: '/plan',
  name: '生产计划管理-生产计划管理',
  component: () => import('@/views/Plan/Plan'),
  meta: {
      title: '生产计划管理-生产计划管理'
  },
},
{
  path: '/commondform',
  name: '生产计划管理-客户需求单管理',
  component: () => import('@/views/Plan/CustomOrderFormManage'),
  children: [
    {
      path: 'apply/:formNo',
      component: () => import('@/components/Plan/apply/Apply'),
      meta:{
        title:'申请-客户需求单管理-生产计划管理'
      }
    }
  ],
  meta: {
      title: '客户需求单管理-生产计划管理'
  },
}
]

