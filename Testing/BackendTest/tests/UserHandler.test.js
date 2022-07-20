const {getAllUsers, getOneUser, addNewUser, updateUser,
  getUIPreferences, updateUIPreferences, getLikedLocations, addLikedLocation,
  deleteLikedLocation, getLikedEvents, addLikedEvents, deleteLikedEvents} = require('./UserHandlerCalls');
const axios = require('axios');

jest.mock('axios');


// UserHandler
it('Gets all the user data in the database', async () => {

  data = [
    {
      id: 1002,
      name: 'Jack',
      likedEvents: [
        "Jerico Beach Biking",
        "21 Savage Concert"
      ]
    },
    {
      id: 1005,
      name: 'Mike',
      likedEvents: [
        "Football Drop-in",
        "Jerico Beach Biking",
        "21 Savage Concert"
      ]
    }
  ]

  axios.get.mockResolvedValue(
    {data,
      status: 200}
  );

  const users = await getAllUsers();
  expect(users.data).toEqual(data);
});

it('Gets one user data from the database', async () => {
  data = {
    id: 1002,
    name: 'Jack',
    likedEvents: [
      "Jerico Beach Biking",
      "21 Savage Concert"
    ]
  }
  axios.get.mockResolvedValue(
    {data,
      status: 200}
  );

  const user = await getOneUser();
  expect(user.data).toEqual(data);
});

it('Adds a new user to the database', async () => {
  
  axios.post.mockResolvedValue(
    {
    status: 200
  }
  );

  const res = await addNewUser();
  expect(res.status).toEqual(200);
});

it('Updates an existing user', async () => {
  
  axios.put.mockResolvedValue(
    {
    status: 200
  }
  );

  const res = await updateUser();
  expect(res.status).toEqual(200);
});

// UI Preferences:

it('Gets the UI prefs of the current user', async () => {

  data = 
    {
      "component1":{
        "URL": "http://abcd123.ca"
      },
      "component2":{
        "length": "10"
      },
      "color1" : "blue", 
    }

  axios.get.mockResolvedValue(
    {data,
      status: 200}
  );

  const res = await getUIPreferences();
  expect(res.data).toEqual(data);
});

it('Updates UI preferences of the current user', async () => {
  axios.put.mockResolvedValue(
    {status: 200}
  );

  const res = await updateUIPreferences();
  expect(res.status).toEqual(200);
});

// Liked Events:

it('Gets liked events of the user', async () => {

  data = 
    {
      "Liked-events": [
        "Event1",
        "Event2"
      ]
    }

  axios.get.mockResolvedValue(
    {data,
      status: 200}
  );

  const res = await getLikedEvents();
  expect(res.data).toEqual(data);
});

it('Adds a liked Event', async () => {
  axios.post.mockResolvedValue(
    {status: 200}
  );

  const res = await addLikedEvents();
  expect(res.status).toEqual(200);
});

it('Deletes a liked event', async () => {
  axios.delete.mockResolvedValue(
    {status: 200}
  );

  const res = await deleteLikedEvents();
  expect(res.status).toEqual(200);
});

// Liked Locations:

it('Gets liked locations of the user', async () => {

  data = 
    {
      "Liked-events": [
        "Loc1",
        "Loc2"
      ]
    }

  axios.get.mockResolvedValue(
    {data,
      status: 200}
  );

  const res = await getLikedLocations();
  expect(res.data).toEqual(data);
});

it('Adds a liked Location', async () => {
  axios.post.mockResolvedValue(
    {status: 200}
  );

  const res = await addLikedLocation();
  expect(res.status).toEqual(200);
});

it('Deletes a liked location', async () => {
  axios.delete.mockResolvedValue(
    {status: 200}
  );

  const res = await deleteLikedLocation();
  expect(res.status).toEqual(200);
});