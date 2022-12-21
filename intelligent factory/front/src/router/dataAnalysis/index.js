module.exports = [
    {
        path: '/dataAnalysis/productionSchedule',
        name: '数据分析报表-生产计划进度',
        component: () => import('@/views/dataAnalysis/ProductionSchedule'),
        meta: {
            title: '生产计划进度-数据分析报表'
        },
    },
    {
        path: '/dataAnalysis/materialStock',
        name: '数据分析报表-物料库存',
        component: () => import('@/views/dataAnalysis/MaterialStock'),
        meta: {
            title: '物料库存-数据分析报表'
        },
    },
    {
        path: '/dataAnalysis/badStatistics',
        name: '数据分析报表-不良统计',
        component: () => import('@/views/dataAnalysis/BadStatistics'),
        meta:{
            title: '不良统计-数据分析报表'
        },
    },
    {
        path: '/productionPlanSchedule/:id',
        name: '产品计划单情况-数据分析报表-生产计划进度',
        component: () => import('@/views/dataAnalysis/ProductionPlanSchedule'),
        meta: {
            title: '计划单-数据分析报表-生产计划进度'
        },
    },
    {
        path: '/productionProcessSchedule/:id',
        name: '对应过程单情况-数据分析报表-生产计划进度',
        component: () => import('@/views/dataAnalysis/ProductionProcessSchedule'),
        meta: {
            title: '对应过程单情况-数据分析报表-生产计划进度'
        },
    }

]