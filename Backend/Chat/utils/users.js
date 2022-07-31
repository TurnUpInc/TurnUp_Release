const users = [];

// Join user to the chat:
function userJoin(socketID, userID, userDatafromDB){
    try{
        var likedEvents = userDatafromDB.likedEvents
        var name = userDatafromDB.name
        const user = {socketID, userID, name, likedEvents};
        users.push(user);
        return user;}
    catch(err){
        console.log(err)
    }
}

// Get current user:
function getCurrentUser(socketID){
    return users.find(user=> user.socketID === socketID);
}

// Get user likedEvents:
function getUserEvents(socketID){
    var events
    try{
        events = users.find(user=> user.socketID ===socketID).likedEvents;
    }
    catch(err){
        console.log("Error getting user events")
    }
    return events
}

// User leaves the chat:
function userLeave(socketID){
    const index = users.findIndex(user => user.socketID === socketID);

    if(index !== -1){
        return users.splice(index, 1)[0];
    }
}


// Get room users: 
function getRoomUsers(event) {
    return users.filter(user => user.likedEvents === event);
}

module.exports = {
    userJoin,
    getCurrentUser,
    userLeave,
    getRoomUsers,
    getUserEvents
}