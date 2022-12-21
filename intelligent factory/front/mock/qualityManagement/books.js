
const books = {
    "books|8":[
        {
            "id|+1":1,
            // "name|1": ["瑕疵1","瑕疵2","瑕疵3","瑕疵4","瑕疵5"],
            "date": '@datetime(yyyy-MM-dd HH:mm:ss)',
            "product": '@string("lower", 5)',
            "workshop":'@string("lower", 5)',
            "productionLine":'string',
            "total|18-28":25,
            "unqualifiedNum|0-5":5,
            "problemProcess":'@string("lower", 5)',
            "processPersonnel":'@cname',
            "description":'@paragraph',
            "defect":{
                "id|+1": 1,
                "name": '@string("lower", 5)'
            }
        }],
}

module.exports = [
    {
        url: 'data/books',
        type: 'get',
        response: config => {
            // const { username } = config.body
            // const token = tokens[username]
            return {
                code: 200,
                data: books
            }
        }
    },

]
