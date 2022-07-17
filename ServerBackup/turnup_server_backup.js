var express = require('express');
var app = express();

const {MongoClient} = require('mongodb')
const uri = 'mongodb://localhost:27017'

const client = new MongoClient(uri)

app.use(express.json())

app.get('/', (req, res) => {
    res.send('TurnUp Server running!')
})

app.post('/', (req, res) => {
    res.send(req.body.text)
})

app.get('/events', async (req, res) => {
    try {
        const result =  await client.db('turnup').collection('event').find().sort({ 'rating' : -1 }).toArray()
        res.status(200).send(result)
    } 
    catch(err) {
        console.log(err)
        res.send(400).send('No Events!')
    }
})

app.get('/events/:category', async (req, res) => {
    try {
        let category = req.params.category
        if (category == 'club'){
            category = 'Club/Concert/Party'
        }
        const result =  await client.db('turnup').collection('event').find({'category': category}).toArray()
        res.status(200).send(result)
    } 
    catch(err) {
        console.log(err)
        res.send(400).send('No Events!')
    }
})

app.get('/likedevents/:user', async(req, res) => {
    try {
        let creatorID = req.params.user
        const result = await client.db('turnup').collection('event').find(
            { 'likedBy': { $elemMatch: { $eq: creatorID } } }
         ).toArray()
        res.status(200).send(result)
    }
    catch(err){
        console.log(err)
        res.send(400).send('No Liked Events!')
    }
})

app.get('/myevents/:user', async(req, res) => {
    try {
        let creatorID = req.params.user
        const result = await client.db('turnup').collection('event').find({'creator': creatorID}).toArray()
        res.status(200).send(result)
    }
    catch(err){
        console.log(err)
        res.send(400).send('No Liked Events!')
    }
})

app.post('/event/:user', async (req, res) => {
    try {
        let creatorID = req.params.user
        req.body.creator = creatorID
        let x = Math.round((Math.random() * (4.5 - 4 + 1) + 3) * 10) / 10
        req.body.rating = x
        let likedBy = [creatorID]
        req.body.likedBy = likedBy
        await client.db('turnup').collection('event').insertOne(req.body)
        await client.db('UserDB').collection('users').updateOne(
            { 'id': creatorID, 'name': creatorID},
            { $push: { 'likedEvents': req.body.title } }, 
            { upsert: true }
         )
        res.status(200).send('New Event Added!')
    } 
    catch(err){
        console.log(err)
        res.send(400).send(err)
    }
})

app.post('/eventliked/:title/:user', async (req,res) => {
    try {
        let userid = req.params.user
        let title = req.params.title
        await client.db('turnup').collection('event').updateOne(
            { 'title': title },
            { $push: { 'likedBy': userid } }
         )
        await client.db('UserDB').collection('users').updateOne(
            { 'id': userid, 'name': userid},
            { $push: { 'likedEvents': title } }, 
            { upsert: true }
         )
         res.status(200).send('Event Liked!')
    }
    catch(err){
        console.log(err)
        res.send(400).send(err)
    }
})


app.put('/event/:user/:id', async (req, res) => {
    try {
        let creatorID = req.params.user
        let EventID = req.params.id
        await client.db('turnup').collection('event').updateOne({'creator': creatorID, 'title':EventID}, { $set: 
            { 
                'title': req.body.title,
                'location': req.body.location,
                'coordinates': req.body.coordinates,
                'description': req.body.description,
                'photoURL': req.body.photoURL,
                'category': req.body.category,
                'date': req.body.date
            }
        })
        res.status(200).send('Event Modified!')
    } 
    catch(err){
        console.log(err)
        res.send(400).send(err)
    }
})

app.delete('/event/:title', async (req, res) => {
    try {
        await client.db('turnup').collection('event').deleteOne({'title': req.params.title})
        res.status(200).send(req.params.title + ' Deleted!')
    } 
    catch(err){
        console.log(err)
        res.send(400).send(err)
    }
})

app.get('/locations', async (req, res) => {
    try {
        const result =  await client.db('turnup').collection('location').find().toArray()
        res.status(200).send(result)
    } 
    catch(err){
        console.log(err)
        res.send(400).send('No Locations!')
    }
})

app.post('/location', async (req, res) => {
    try {
        req.body.likes = 1;
        req.body.vists = 1;
        await client.db('turnup').collection('location').insertOne(req.body)
        res.status(200).send('New location Added!')
    } 
    catch(err){
        console.log(err)
        res.send(400).send(err)
    }
})

app.post('/likelocation/:title', async (req, res) => {
    try {
        let loc_title = req.params.title
        await client.db('turnup').collection('location').updateOne({ 'title': loc_title },{ $inc: { 'likes': 1 }});
        res.status(200).send('Location Liked! \n')
    } 
    catch(err){
        console.log(err)
        res.send(400).send(err)
    }
})


async function run() {
    try {
        await client.connect()
        console.log('Successfully conntected to the Database!')
        var server = app.listen(8081, (req, res) => {
            var host = server.address().address
            var port = server.address().port
            console.log('Example sever running at http://%s:%s', host, port)
        })
    }
    catch(err) {
        console.log(err);
        await client.close()
    }
}

run()