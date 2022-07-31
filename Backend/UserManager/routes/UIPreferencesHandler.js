var express = require("express")
const router = express.Router();

const axios = require('axios').default;

// Routes:

/**
 *  Fetches the UIPreferences of the user
 */
 router.get("/:UserId", async (req, res)=>{
    try{
        let response
        await axios.get('http://localhost:8080/users/'+req.params.UserId).then(resp => {
            response = resp.data;
        });
        console.log(response)
        res.status(200).send(response.UIPreferences) 
    }
    catch(err){
        console.log(err)
        res.send(400).send(err)
    }
})


/**
 * Modifies the UIPrefences of the user
 */
 router.put("/:UserId", async (req, res)=>{
    try{
        await axios.put('http://localhost:8080/users/'+req.params.UserId, {
            "UIPreferences" : req.body
        });
        res.status(200).send("Success: New ui-preference: "+req.body)
    }
    catch(err){
        console.log(err)
        res.send(400).send(err)
    }
})

module.exports = router
