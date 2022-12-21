module.exports = [
  {
    path: '/storehouse/storageMain',
    name: 'storage',
    component: () => import('@/views/storehouse/storehouswMain'),
    meta: {
      title: '仓库管理-库区管理',
    },
    children:[
      {
        path:'',
        redirect:'SectorInfo'
      },
      {
        path:'SectorInfo',
        name: 'sad',
        component: () =>import('@/components/storehouse/tableSector'),
        meta:{
          title: '库区管理'
        }
      },
      {
        path: 'ShelfInfo',
        name: 'shelf',
        component: () => import('@/components/storehouse/tableShelf'),
        meta: {
          title: '货架管理'
        },
      },
      {
        path:'addSector',
        name:'',
        component: () => import('@/components/storehouse/increSector'),
        meta: {
          title: "增加库区"
        }
      },
      {
        path:'shelfInfoDetail',
        name:'',
        component: () => import('@/components/storehouse/shelfInfo'),
        meta: {
          title: '货架详情'
        }
      }
    ]
  },
  {
    path: '/storehouse/equAndMatMain',
    name: 'item',
    component: () => import('@/views/storehouse/Equ_and_mat'),
    meta: {
      title: '仓库管理-物料设备'
    },
    children:[
      {
        path:'',
        redirect:'materialInfo'
      },
      {
        path:'equipmentInfo',
        name: 'sad',
        component: () =>import('@/components/storehouse/equAndMat/equmentInfo'),
        meta:{
          title: '仓库管理-设备管理'
        },
      },
      {
        path:'equipmentInfoDetail',
        name:'sad',
        component: ()=> import('@/components/storehouse/equAndMat/equipmentInfoDetail'),
        meta: {
          title: '设备管理-设备信息'
        }
      },
      {
        path:'materialInfo',
        component:()=> import('@/components/storehouse/equAndMat/materialInfo'),
        meta: {
          title: '仓库管理-物料管理'
        }
      },
      {
        path: 'materialInfoDetail',
        component: ()=> import('@/components/storehouse/equAndMat/meterialInfoDetail'),
        meta: {
          title: '仓库管理-物料信息'
        }
      },
      {
        path: 'searchItem',
        component:()=> import('@/components/storehouse/equAndMat/searchItem'),
        meta: {
          title: '仓库管理-查询信息'
        }
      }

    ]
  },
  {
    path: '/storehouse/inOut',
    name: 'storageManager',
    component: () => import('@/views/storehouse/in_out_storage'),
    meta: {
      title: '仓库管理-出入库管理'
    },
    children: [
      {
        path: '',
        redirect: 'itemRequest',
      },
      {
        path:'itemRequest',
        name:'reques',
        component: () => import('@/components/storehouse/in_out/itemRequest'),
        meta: {
          title: '出入库管理-请求信息'
        }
      },
      {
        path:'itemManage',
        name:'manange',
        component: ()=> import('@/components/storehouse/in_out/inOutManagement'),
        meta: {
          title: '出入库管理-出入库操作'
        }
      },
      {
        path: 'itemRequestRecords',
        name: 'records',
        component: () => import('@/components/storehouse/in_out/inOutManagementRecords'),
        meta: {
          title: '出入库管理-出入库记录'
        }
      },
      {
        path: 'apply',
        name:'apply',
        component: ()=>import('@/components/storehouse/in_out/itenApply'),
        meta: {
          title: '物料设备申请'
        }
      }
    ]
  },
  {
    path: '/storehouse/inOutApproval',
    name:'storageApp',
    component: () => import('@/views/storehouse/in_out_approval'),
    meta: {
      title: '仓库管理-出入库审批'
    },
    children: [
      {
        path: '',
        redirect:'itemapproval'
      },
      {
        path: 'itemapproval',
        name: 'itemapproval',
        component: () => import('@/components/storehouse/in_out/itemapproval'),
        meta: {
          title: '仓库管理-出库管理'
        }
      },
      {
        path: 'equipmentInfoDetail',
        name: 'equipmentInfoDetail',
        component: () => import('@/components/storehouse/in_out/equAndMateApply'),
        meta: {
          title: '仓库管理-出库管理'
        }
      }
    ]
  }

];