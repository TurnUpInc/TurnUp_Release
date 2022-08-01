const express = require("express");
const app = express();
app.use(express.json());

const eventStore = require("./routes/eventStore");
app.use("/events", eventStore);

const eventFilter = require("./routes/eventFilter");
app.use("", eventFilter);

module.exports = app;
