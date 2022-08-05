const express = require("express");
const router = express.Router();
const { MongoClient } = require("mongodb");
const client = new MongoClient("mongodb://20.122.91.139:2541");

// Gets event corresponding to the EventId
router.get("/:EventID", async (req, res) => {
  try {
    let eventID = req.params.EventID;
    const payload = await client
      .db("turnup")
      .collection("event")
      .findOne({ e_id: eventID });
    if (payload) {
      res.status(200).json(payload);
    } else {
      res.status(404).json({});
    }
  } catch (err) {
    console.log(err);
    res.status(400).send(err);
  }
});

// Creates new event with creator set to UserId & returns a unique EventId
router.post("/:UserID", async (req, res) => {
  try {
    let userId = req.params.UserID;
    let payload = req.body;
    Object.assign(payload, { Creator: userId });
    await client.db("turnup").collection("event").insertOne(payload);
    let eventId = payload._id.toString();
    await client
      .db("turnup")
      .collection("event")
      .updateOne({ _id: payload._id }, { $set: { e_id: eventId } });
    res.status(200).send(eventId);
  } catch (err) {
    console.log(err);
    res.status(400).send(err);
  }
});

// Updates event with new payload given correct EventId and UserId
// router.put("/:EventID/:UserID", async (req, res) => {
//   try {
//     let eventId = req.params.EventID;
//     let userId = req.params.UserID;
//     let stats = await client
//       .db("turnup")
//       .collection("event")
//       .updateOne(
//         { e_id: eventId, Creator: userId },
//         { $set: req.body },
//         { upsert: false }
//       );
//     if (stats.matchedCount === 0)
//       res.status(400).send("Event not found or you are not the creator");
//     else res.status(200).send("Event modified successfully\n");
//   } catch (err) {
//     console.log(err);
//     res.status(400).send(err);
//   }
// });

router.put("/event/:user/:id", async (req, res) => {
  try {
    let creatorID = req.params.user;
    let EventID = req.params.id;
    await client
      .db("turnup")
      .collection("event")
      .updateOne(
        { creator: creatorID, title: EventID },
        {
          $set: {
            title: req.body.title,
            location: req.body.location,
            coordinates: req.body.coordinates,
            description: req.body.description,
            photoURL: req.body.photoURL,
            category: req.body.category,
            date: req.body.date,
          },
        }
      );
    res.status(200).send("Event Modified!");
  } catch (err) {
    console.log(err);
    res.send(400).send(err);
  }
});

// Deletes event with new payload given correct EventId and UserId
// router.delete("/:EventID/:UserID", async (req, res) => {
//   try {
//     let eventID = req.params.EventID;
//     let userId = req.params.UserID;
//     let event = await client
//     .db("turnup")
//     .collection("event")
//     .findOne({ e_id: eventID});
//     if (event === null) {
//       res.status(404).send("Event not found");
//       return;
//     }
//     let stats = await client
//       .db("turnup")
//       .collection("event")
//       .deleteOne({ e_id: eventID, Creator: userId });
//     if (stats.deletedCount === 0)
//       res.status(400).send("You are not the creator");
//     else res.status(200).send("Event deleted successfully");
//   } catch (err) {
//     console.log(err);
//   }
// });

router.delete("/event/:title", async (req, res) => {
  try {
    await client
      .db("turnup")
      .collection("event")
      .deleteOne({ title: req.params.title });
    res.status(200).send(req.params.title + " Deleted!");
  } catch (err) {
    console.log(err);
    res.send(400).send(err);
  }
});

// Gets all events
router.get("/", async (req, res) => {
  try {
    const result = client.db("turnup").collection("event").find();
    const payload = await result.toArray();
    res.status(200).send(Object.assign(payload));
  } catch (err) {
    console.log(err);
    res.status(400).send(err);
  }
});

router.post("/:user", async (req, res) => {
  try {
    let creatorID = req.params.user;
    req.body.creator = creatorID;
    let x = Math.round((Math.random() * (4.5 - 4 + 1) + 3) * 10) / 10;
    req.body.rating = x;
    let likedBy = [creatorID];
    req.body.likedBy = likedBy;
    await client.db("turnup").collection("event").insertOne(req.body);
    await client
      .db("UserDB")
      .collection("users")
      .updateOne(
        { id: creatorID, name: creatorID },
        { $push: { likedEvents: req.body.title } },
        { upsert: true }
      );
    res.status(200).send("New Event Added!");
  } catch (err) {
    console.log(err);
    res.send(400).send(err);
  }
});


module.exports = router;
