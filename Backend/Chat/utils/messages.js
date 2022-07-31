const moment = require('moment');

function formatMessage(username, text, eventID){

    return{
        username,
        eventID,
        text,
        time: moment().format('h:mm a'),
    }
}

module.exports = formatMessage;