const express = require("express");
const app = express();
require("dotenv").config();
app.use(express.json());

// const eventHandler = require('./routes/eventHandler');
// app.use('/event', eventHandler);

const eventStore = require("./routes/eventStore");
app.use("/events", eventStore);

const eventFilter = require("./routes/eventFilter");
app.use("", eventFilter);

// MongoClient.connect(process.env.DB_SECRET, (err, client) => {
//   if (err) throw err;
//   console.log("Connected to MongoDB");
//   app.listen(8000);
// });
// async function start() {
//   try {
//     await client.connect();
//     console.log("Connected to MongoDB");
//     app.listen(8000);
//   } catch (err) {
//     console.log(err);
//     await client.close();
//   }
// }
// start();
app.listen(8000);

app.get("/", (req, res) => {
  res.send("Hello");
});

