
const quality = {
    "userList|10":[
        {
            "name":"@cname",
            "age|18-28":25,
            "sex|1":["男","女"],
        }],
}

module.exports = [
    {
        url: 'data/quality',
        type: 'post',
        response: config => {
            // const { username } = config.body
            // const token = tokens[username]
            return {
                code: 200,
                data: quality
            }
        }
    },

]
