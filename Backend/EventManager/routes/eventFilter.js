const express = require("express");
const router = express.Router();
// const axios = require("axios").default;
const { MongoClient } = require("mongodb");
const client = new MongoClient(process.env.DB_SECRET);

// Gets event corresponding to the category
router.get("/events-by-category", async (req, res) => {
  try {
    let category = req.query.cat;
    console.log(category);
    const result = client
      .db("EventManager")
      .collection("events")
      .find({ category });
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
      .db("EventManager")
      .collection("events")
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

module.exports = router;
