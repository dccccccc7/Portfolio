

const workshopItem = {
  "tableData|15": [
    {
      "id|1-100": 1,
      "name": '@ctitle',
      "factory_name": "@city",
      "lineNum|3-10":3,
      "stationNum": "@integer(1,100)",
      "created_time": '@date',
      "modified_time": '@date'

    }
  ],
}
const lineItem = {
  "lineItem|15": [
    {
      'lineId|300-400': 300,
      'lineName': '@cname()',
      'lineEqu|15-30': 19,
      'stateNum|5-10':7,
      'createDate': '@date',
      'updateDate':'@date'
    }
  ]
}
const workshopDetail = {
  "workshopDetail|1": [
    {
      "id|1-100":1,
      "name": "@cname",
      "factory":"@city",
      "size|3-5":3,
      "productionLine|4": [
        {
          'lineId|300-400': 300,
          'lineName': '@cname()',
          'lineEqu|15-30': 19,
          'stateNum|5-10':7,
          'createDate': '@date',
          'updateDate':'@date'
        }
      ]
    }

]
}
const workshopInfo = {
  'workshopInfo|1':[
    {
      'id|1-100':5,
      'name':'@cname',
      'factory_name': '@city',
      'lines|10':[
        {
          'id|10-20':15,
          'name' : '@cname',
          'stationNum':5,
          'stations|16':[
            {
              'id|1-10':5,
              'stationOrder|1-10':5,
              'equipment': '@ctitle',
              'equip_id': '@ctitle',
              'user':'@cname'
            },
          ]
        }
      ]
    }
  ]
}

module.exports = [
  {
    url: 'workshop/workshopItem',
    type: 'get',
    response: config => {
      // print(config)
      return {
        data: workshopItem
      }
    }
  },
  {
    url: 'workshop/workshopDetail',
    type: 'get',
    response: config => {

      return workshopDetail
    }
  },
  {
    url: 'workshop/lineItem',
    type: 'get',
    response: config => {
      return lineItem
    }
  },
  {
    url: 'workshop/mainInfo',
    type: 'get',
    response: config => {
      return workshopInfo
    }
  }
];