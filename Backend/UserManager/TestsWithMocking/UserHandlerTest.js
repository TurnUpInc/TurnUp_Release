var express = require("express")
var app = express()

const userStoreRoute = require('../routes/UserStore')
const uiPreferencesRoute = require('../routes/UIPreferencesHandler')
const likedLocationsRoute = require('../routes/LikedLocationsHandler')
const likedEventsRoute = require('../routes/LikedEventsHandler')

app.use(express.json())

// Routes:
app.use("/users", userStoreRoute)
app.use("/ui-preference", uiPreferencesRoute)
app.use("/liked-locations", likedLocationsRoute)
app.use("/liked-events", likedEventsRoute)

module.exports = app;