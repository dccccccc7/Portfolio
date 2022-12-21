module.exports = [
  {
    url: 'data/systemManagement/userList',
    type: 'get',
    response: config => {
      // const { username } = config.body
      // const token = tokens[username]
      return {
        "code|1":[200,200],
        "message":"hahahah",
        "userList|50": [
          {
            "id": "@id",
            "name": "@cname",
            "user_name": "admin",
            "age|18-28": 25,
            "sex|1": ["男", "女"],
            "role|1": ['项目经理', '员工', '部门经理'],
            "department|1": ['项目经理', '员工', '部门经理'],
            "password":"@ip",
            "date":"@date",
          }],
        "positionList": [
          {text: '项目经理', value: '项目经理'},
          {text: '员工', value: '员工'},
          {text: '部门经理', value: '部门经理'}],
        "departmentList": [
          {text: '项目经理', value: '项目经理'},
          {text: '员工', value: '员工'},
          {text: '部门经理', value: '部门经理'}],
      }
    }
  },
]
