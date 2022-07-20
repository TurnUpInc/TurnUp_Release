// mongo:
const mongo = require('mongodb').MongoClient;

// SocketIO, Express deps
const path = require('path');
const http = require('http');
const express = require('express');
const socketio = require('socket.io');
const bodyParser = require('body-parser');
const { userJoin, getCurrentUser, userLeave, getUserEvents } = require('./utils/users')

const formatMessage = require('./utils/messages')

const app = express();
const server = http.createServer(app);
const io = socketio(server);

// Set static folder:

app.use(express.static(path.join(__dirname, 'public')))
app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());


mongo.connect('mongodb://20.122.91.139:27017', function(err, client){
    if(err){
        throw err;
    }
    console.log("Database connected!");
    connectChat(client);
})


/**
 * Chat Server
 * @param {*} db Database sourse that has 'chat' and 'UserDB' databases
 *  'UserDB' database is the same database as the one used by UserManager
 */
function connectChat(db){
    io.on('connection', socket => {
        // Get user and Chat database
        chatDB = db.db('chat')
        userDB = db.db('UserDB')

        /**
         *  Function to send status
         */ 
        sendStatus = function(s){
            socket.emit('status', s);
        }

        /**
         * Connects the user with userID to the chat, connects the socket to 'likedEvents' rooms
         */
        socket.on('connectUser', async (userID)=>{
            // Get user info from the DB
            userDatafromDB = await userDB.collection("users").find({"id":userID}).toArray();
            console.log(userDatafromDB)
            // Adds user info to the user Array
            userJoin(socket.id, userID, userDatafromDB[0])
            // Gets user Events and connectes them to the corresponding rooms
            socket.join(getUserEvents(socket.id))
        })

        /**
         * Gets all the chat history of 'eventID' room
         */
        socket.on('getChatHistory', async (eventID)=>{
            try{
                console.log("Getting chat history: ")
                
                if(getUserEvents(socket.id).includes(eventID)){
                    chatDB.collection(eventID).find().limit(100).sort().toArray(function(err, res){
                        if(err){
                            throw err
                        }
                        console.log("Chat history:\n")
                        console.log(res)
                        socket.emit('output'+eventID, res)
                    });
                }
            }
            catch(err){
                console.log("User doesn't contain this Event in the likedEvents list:: Error:"+err.message);
                sendStatus("User doesn't contain this Event in the likedEvents list:: Error:"+err.message);
            }
        })

        /**
         * Call when user likes a new event:
         * Adds user to the event room, and adds the event to the local liked-events list
         */
        socket.on('joinRoom', ( eventID )=>{
            // Adds the event to local Liked-Events list:
            try{
              if(getUserEvents(socket.id).indexOf(eventID)===-1){
                  getUserEvents(socket.id).push(eventID)
              }
              console.log("Added user to the new event room")
              console.log(getUserEvents(socket.id))
              // Connects the current socket to the room
              socket.join(eventID);
            }
            catch(err){
              console.log("Events not found");
              sendStatus("Events not found");
            }
        });

        /**
         * Sends 'msg' chat messages to all the users of the 'eventID' event 
         */
        socket.on('chatMessage', (eventID, msg)=>{
            // Sends message to all users including the sender
                // first checks if the user has the event in likedEvents list
            currentChat = chatDB.collection(eventID);
            if(msg == '' || eventID==''){
                sendStatus("Message or EventID can't be null");
            }
            else{
                try{
                    if(getUserEvents(socket.id).includes(eventID)){
                        // Insert the chat into the chat DB:
                        message = formatMessage(getCurrentUser(socket.id).name, msg, eventID)
                        currentChat.insertOne(message, function(){
                            console.log(message.username+" sending message: \n"+message.text)
                            io.to(eventID).emit('message',  message)
                        });

                    }
                    else{
                        sendStatus("Event not found in the database")
                    }
                }
                catch{
                    sendStatus("Couldn't find the linked event in user DB")
                }
            }
        })

        /**
         * Removes the user from the local list upon Un-Liking an event
         */
        socket.on('unsubscribe', ()=>{
            userLeave(socket.id);
        });
    });
}
app.set('port', (process.env.PORT || 5000));

server.listen(app.get('port'), ()=> console.log(`Server running on port: ${app.get('port')}`))