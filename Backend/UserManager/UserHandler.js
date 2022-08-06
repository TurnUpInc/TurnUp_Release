var express = require("express")
var app = express()

const userStoreRoute = require('./routes/UserStore')
const uiPreferencesRoute = require('./routes/UIPreferencesHandler')
const likedLocationsRoute = require('./routes/LikedLocationsHandler')
const likedEventsRoute = require('./routes/LikedEventsHandler')

const {MongoClient} = require("mongodb")
const uri = "mongodb://20.122.91.139:2541"
const client = new MongoClient(uri)

app.use(express.json())


// Routes:
app.use("/users", userStoreRoute)
app.use("/ui-preference", uiPreferencesRoute)
app.use("/liked-locations", likedLocationsRoute)
app.use("/liked-events", likedEventsRoute)

async function run(){
    try{
        await client.connect()
        console.log("Successfully connected to the database")


        var server = app.listen(8080, (req, res)=>{
            var host = server.address().address
            var port = server.address().port
            console.log("UserStore successfully running at: http://%s:%s", host, port)
        
        })
    }
    catch(err){
        console.log(err)
        await client.close()
    }
}

run()