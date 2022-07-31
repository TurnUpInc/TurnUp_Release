var express = require("express")
const router = express.Router();

const {MongoClient} = require("mongodb")
const uri = "mongodb://localhost:27017"
const client = new MongoClient(uri)

// Routes:

/**
 * Fetches all the registered users in the Database
 */
 router.get("/", async (req, res)=>{
    try{
        const result = await client.db("UserDB").collection("users").find().toArray()
        res.status(200).send(result) 
    }
    catch(err){
        console.log(err)
        res.send(400).send(err)
    }
})

/**
 *  Fetches the registered user with UserID as ID in the Database
 */
 router.get("/:UserId", async (req, res)=>{
    try{
        id = req.params.UserId;
        var result = await client.db("UserDB").collection("users").find(id).toArray()
        res.status(200).json(result[0]) 
    }
    catch(err){
        console.log(err)
        res.send(400).send(err)
    }
})


/**
 * Adds a new user to the Database
 */
 router.post("/", async (req, res)=>{
    try{
        console.log("adding user: "+ req.body.toString())
        // Generates random ID:
        var id = Math.random().toString(16).slice(2)

        var userInfo = req.body
        userInfo.id = id
        await client.db("UserDB").collection("users").insertOne(userInfo)
        res.status(200).send("User added successfully\n") 
    }
    catch(err){
        console.log(err)
        res.send(400).send(err)
    }
})

/**
 * Updates users info in the Database
 */
 router.put("/:UserId", async (req, res)=>{
    try{
        var id = req.params.UserId;
        req.body.id = id
        await client.db("UserDB").collection("users").updateOne(id,  { "$set" : req.body  }, {upsert:false})
        res.status(200).send("User updated successfully\n") 
    }
    catch(err){
        console.log(err)
        res.send(400).send(err)
    }
})

/**
 * Deletes user from the database
 */
 router.delete("/:UserId", async (req, res)=>{
    try{
        var id = req.params.UserId;
        await client.db("UserDB").collection("users").deleteMany(id)
        res.status(200).send("User deleted successfully\n") 
    }
    catch(err){
        console.log(err)
        res.send(400).send(err)
    }
})

module.exports = router