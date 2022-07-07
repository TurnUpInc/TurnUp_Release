var express = require("express")
const router = express.Router();

const axios = require('axios').default;


/**
 *  Fetches the liked events of the requested user
 */
 router.get("/:UserId", async (req, res)=>{
    try{
        let response
        await axios.get('http://localhost:8080/users/'+req.params.UserId).then(resp => {
            response = resp.data;
        });
        console.log(response)
        res.status(200).send(response.likedEvents) 
    }
    catch(err){
        console.log(err)
        res.send(400).send(err)
    }
})


/**
 * Adds a event to the liked events list
 */
 router.post("/:UserId/:EventID", async (req, res)=>{
    try{
        req.body.id = req.params.UserId;
        let user_data
        await axios.get('http://localhost:8080/users/'+req.params.UserId).then(resp => {
            user_data = resp.data;
        });
        const event_arr = user_data.likedEvents
        if(!event_arr.includes(req.params.EventID)){
            event_arr.push(req.params.EventID)
        }
        console.log(event_arr)        
        const result = await axios.put('http://localhost:8080/users/'+req.params.UserId, {
            "likedEvents" : event_arr
        });
        res.status(200).send("Success: New liked-events: "+event_arr)
    }
    catch(err){
        console.log(err)
        res.send(400).send(err)
    }
})

/**
 * Deletes user from the database
 */
 router.delete("/:UserId/:EventID", async (req, res)=>{
    try{
        req.body.id = req.params.UserId;
        let user_data
        await axios.get('http://localhost:8080/users/'+req.params.UserId).then(resp => {
            user_data = resp.data;
        });
        const event_arr = user_data.likedEvents
        const index = event_arr.indexOf(req.params.EventID);
        if (index > -1) {
            event_arr.splice(index, 1);
        }
        console.log(event_arr)        
        const result = await axios.put('http://localhost:8080/users/'+req.params.UserId, {
            "likedEvents" : event_arr
        });
        res.status(200).send("Success: New liked-events: "+event_arr)
    }
    catch(err){
        console.log(err)
        res.send(400).send(err)
    }
})

module.exports = router
