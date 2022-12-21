module.exports = [
  {
    path: '/test/baseTest/createPro',
    name: 'createPro',
    component: () => import('@/views/workshop/createPro'),
    meta: {
      title: '测试组件2-新建仓库',
      keepAlive: true
    }
  },

  {
    path: '/work/apply',
    name: 'apply',
    component: () => import('@/views/workshop/applyPro'),
    meta: {
      title: '申请查询-'
    },
    children:[
      {
        path: '',
        redirect:'workshopInfo'
      },
      {
        path: 'workshopInfo',
        name: 'workshopInfo',
        component: () => import('@/components/baseShop/applyWorkshop'),
        meta: {
          title:'申请查询-车间信息'
        }
      },
      {
        path: 'lineInfo',
        name: 'lineInfo',
        component: () => import('@/components/baseShop/applyLine'),
        meta: {
          title:'申请查询-产线信息'
        }
      },
      {
        path: 'stationInfo',
        name: 'stationInfo',
        component: () => import('@/components/baseShop/applyStation'),
        meta: {
          title:'申请查询-工位信息'
        }
      },
    ]
  },
  {
    path: '/work/workshop',
    name: 'workshop',
    component: () => import('@/views/workshop/theWorkshopMain'),
    meta: {
      title: '车间管理-',
    },
  },
  //---------------------------------
  // {
  //   path: '/work/workshop/workshopDetail',
  //   name: 'workshopDetail',
  //   component: () => import('@/views/workshop/workshopDetail'),
  //   meta: {
  //     title: '车间管理-车间信息'
  //   }
  // },
  {
    path: '/work/workshop/workshopDetail',
    name: 'line',
    component: () => import('@/views/workshop/line'),
    meta: {
      title: '车间管理-车间信息'
    }
  },
  //--------------------------
  {
    path: '/work/line',
    name: 'line',
    component: () => import('@/views/workshop/line'),
    meta: {
      title: '车间管理-产线信息'
    }
  },
  {
    path: '/work/workshop/line/lineDetail',
    name: 'lineDetail',
    component: () => import('@/views/workshop/lineDetail'),
    meta: {
      title: '车间管理-产线详情'
    }
  },
  {
    path: '/work/apply/info',
    name:'applyDetail',
    component: ()=> import('@/views/workshop/applyDeatils'),
    meta: {
      title: '申请信息'
    }
  },



  {
    path: '/aaaTRY',
    name: 'lineDetail',
    component: () => import('@/views/aaaTRY'),
    meta: {
      title: '车间管理-产线详情'
    }
  }
];