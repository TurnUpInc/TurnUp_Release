const express = require("express");
const router = express.Router();
const { MongoClient } = require("mongodb");
const client = new MongoClient("mongodb://20.122.91.139:2541");

// Gets event corresponding to the category
router.get("/events-by-category", async (req, res) => {
  try {
    let category = req.query.cat;
    if (category == "club") category = "Club/Concert/Party";
    console.log(category);
    const result = client.db("turnup").collection("event").find({ category });
    let payload = await result.toArray();
    res.status(200).send(payload);
  } catch (err) {
    console.log(err);
    res.status(400).send(err);
  }
});

// Gets all events sorted in decending order by rating
router.get("/events-by-rating", async (req, res) => {
  try {
    const result = client
      .db("turnup")
      .collection("event")
      .find()
      .sort({ rating: -1 });
    let payload = await result.toArray();
    console.log(payload);
    res.status(200).send(payload);
  } catch (err) {
    console.log(err);
    res.status(400).send(err);
  }
});

//Get liked events
router.get("/likedevents/:user", async (req, res) => {
  try {
    let creatorID = req.params.user;
    const result = await client
      .db("turnup")
      .collection("event")
      .find({ likedBy: { $elemMatch: { $eq: creatorID } } })
      .toArray();
    res.status(200).send(result);
  } catch (err) {
    console.log(err);
    res.send(400).send("No Liked Events!");
  }
});

//Get all of users events
router.get("/myevents/:user", async (req, res) => {
  try {
    let creatorID = req.params.user;
    const result = await client
      .db("turnup")
      .collection("event")
      .find({ creator: creatorID })
      .toArray();
    res.status(200).send(result);
  } catch (err) {
    console.log(err);
    res.send(400).send("No Liked Events!");
  }
});

router.post("/eventliked/:title/:user", async (req, res) => {
  try {
    let userid = req.params.user;
    let title = req.params.title;
    await client
      .db("turnup")
      .collection("event")
      .updateOne({ title }, { $push: { likedBy: userid } });
    await client
      .db("UserDB")
      .collection("users")
      .updateOne(
        { id: userid, name: userid },
        { $push: { likedEvents: title } },
        { upsert: true }
      );
    res.status(200).send("Event Liked!");
  } catch (err) {
    console.log(err);
    res.send(400).send(err);
  }
});


module.exports = router;
