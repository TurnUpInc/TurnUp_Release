var express = require("express")
var app = express()


const eventStore = require("../routes/eventStore");
const eventFilter = require("../routes/eventFilter");

app.use(express.json())

// Routes:
app.use("/events", eventStore);
app.use("", eventFilter);




module.exports = app;