
const defects = {
    "defects|8":[
    {
        "classification": '@string("lower", 5)',
        "defectCode|+1":1,
        "defectName":'@string("lower", 5)',
    }],
}

module.exports = [
    {
        url: 'data/defects',
        type: 'get',
        response: config => {
            // const { username } = config.body
            // const token = tokens[username]
            return {
                code: 200,
                data: defects
            }
        }
    },

]
