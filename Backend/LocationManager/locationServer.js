var express = require('express');
var app = express();
const axios = require('axios').default;

const {MongoClient} = require('mongodb')
const uri = 'mongodb://20.122.91.139:2541'

const client = new MongoClient(uri)

app.use(express.json())



/**
 * Returns all the locations withing a given cordinate range
 */
app.get('/locations-by-coordinates', async (req, res) => {

    let lat1 = req.query.latitude1;
    let long1 = req.query.longitude1;
    let lat2 = req.query.latitude2;
    let long2 = req.query.longitude2;
    //get lat and long
    let returnVal = [];
    let locs 
    await axios.get('http://localhost:8080/locations').then(res => {
        locs = res.data;
    });
    
    locs.array.forEach(element => {
        let loc_lat = element.coordinates[0]
        let loc_long = element.coordinates[1]
        if (loc_long > long1 && loc_lat > lat1 && loc_long < long2 && loc_lat < lat2) {
            returnVal.push(element)
        }
    });

    if (returnVal.length > 0){
        res.status(200).send(returnVal)
    }
    else{
        res.status(404).send('No Events Available!')
    }

})



/**
 * Increments location likes in the Database
 */
app.put('/incrlike/:locationID', async (req, res) => {
    try {
        let locID = req.params.locationID
        await client.db('test').collection('locations').update({ 'l_id': locID },{ $inc: { 'likes': 1 }});
        res.status(200).send('Location Liked! \n')
    } 
    catch {
        console.log(err)
        res.send(400).send(err)
    }
})


/**
 * Increments location visitors in the Database
 */
app.put('/incrvisitors/:locationID', async (req, res) => {

    try {
        let locID = req.params.locationID
        await client.db('test').collection('locations').update({ 'l_id': locID },{ $inc: { 'visitors': 1 }});
        res.status(200).send('Location Visited! \n')
    } 
    catch {
        console.log(err)
        res.send(400).send(err)
    }

})




/**
 * Fetches all the locations in the Database
 */
app.get('/locations', async (req, res) => {
    try {
        const result = await client.db('test').collection('locations').find().toArray()
        res.status(200).send(result)
    } 
    catch {
        console.log(err)
        res.send(400).send(err)
    }
})


/**
 * Adds a new location to the Database
 */
app.post('/locations/:UserID', async (req, res) => {
    try {
        await client.db('test').collection('locations').insertOne(req.body)
        res.status(200).send('Location added successfully \n')
    } 
    catch {
        console.log(err)
        res.send(400).send(err)
    }
})

/**
 * Updates location info in the Database
 */
app.put('/locations/:locationID', async (req, res) => {
    try {
        let locID = req.params.locationID
        await client.db('test').collection('locations').updateOne({'l_id': locID},  { '$set' : req.body  }, {upsert:false})
        res.status(200).send('Location modified successfully \n')
    } 
    catch {
        console.log(err)
        res.send(400).send(err)
    }
})

/**
 * Deletes location from the database
 */
app.delete('/locations/:locationID/', async (req, res) => {
    try {
        let locID = req.params.locationID
        await client.db('test').collection('locations').deleteMany({'l_id': locID})
        res.status(200).send('Todo item deleted successfully \n')
    } 
    catch {
        console.log(err)
        res.send(400).send(err)
    }
})



/**
 *  Fetches the a specific location with LocationID as ID in the Database
 */
app.get('/locations/:locationID', async (req, res) => {
    try {
        let locID = req.params.locationID
        const result = await client.db('test').collection('locations').find({'l_id':locID}).toArray()
        res.status(200).json(result[0]) 
    } 
    catch {
        console.log(err)
        res.send(400).send(err)
    }
})

async function run(){
    try{
        await client.connect()
        console.log("Successfully connected to the database")


        var server = app.listen(8040, (req, res)=>{
            var host = server.address().address
            var port = server.address().port
            console.log("UserStore successfully running at: http://%s:%s", host, port)
        
        })
    }
    catch(err){
        console.log(err)
        await client.close()
    }
}

run()

