var express = require("express")
const router = express.Router();

const axios = require('axios').default;



/**
 *  Fetches the liked locations of the requested user
 */
 router.get("/:UserId", async (req, res)=>{
    try{
        let response
        await axios.get('http://localhost:8080/users/'+req.params.UserId).then(resp => {
            response = resp.data;
        });
        console.log(response)
        res.status(200).send(response.likedLocations) 
    }
    catch(err){
        console.log(err)
        res.status(400).send(err)
    }
})


/**
 * Adds a location to the liked locations list
 */
 router.post("/:UserId/:LocationID", async (req, res)=>{
    try{
        req.body.id = req.params.UserId;
        let user_data
        await axios.get('http://localhost:8080/users/'+req.params.UserId).then(resp => {
            user_data = resp.data;
        });
        const location_arr = user_data.likedLocations
        if(!location_arr.includes(req.params.LocationID)){
            location_arr.push(req.params.LocationID)
        }
        console.log(location_arr)        
        await axios.put('http://localhost:8080/users/'+req.params.UserId, {
            "likedLocations" : location_arr
        });
        res.status(200).send(location_arr)
    }
    catch(err){
        console.log(err)
        res.status(400).send(err)
    }
})

/**
 * Deletes user from the database
 */
 router.delete("/:UserId/:LocationID", async (req, res)=>{
    try{
        req.body.id = req.params.UserId;
        let user_data
        await axios.get('http://localhost:8080/users/'+req.params.UserId).then(resp => {
            user_data = resp.data;
        });
        const location_arr = user_data.likedLocations
        const index = location_arr.indexOf(req.params.LocationID);
        if (index > -1) {
            location_arr.splice(index, 1);
        }
        console.log(location_arr)        
        await axios.put('http://localhost:8080/users/'+req.params.UserId, {
            "likedLocations" : location_arr
        });
        res.status(200).send(location_arr)
    }
    catch(err){
        console.log(err)
        res.status(400).send(err)
    }
})

module.exports = router
