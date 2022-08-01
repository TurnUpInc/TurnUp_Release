const request = require("supertest");
const app = require("./UserHandlerTest");
const axios = require('axios');

jest.mock('axios');

//**********UserStore Tests***************//

describe("GET /users/", () => {
  test("Should return list of all the users", async () => {

    console.log("BEFORE CALL");
    const response = await request(app).get("/users/").send();
    console.log(response.body);
    // expect(response.body).toEqual(data);
    expect(response.status).toBe(200);
  });
});

describe("POST /users/4321", () => {
  test("Adds a user with ID 4321", async () => {

    data = {  
      "name": "Drake",  
      "likedEvents": [    "Football Drop-in",    "Jerico Beach Biking",    "21 Savage Concert"  ]
    }
    console.log("BEFORE CALL");
    const response = await (await request(app).post("/users/4321").send(data));
    console.log(response.body);
    expect(response.status).toBe(200);
  });
});

describe("POST /users/", () => {
  test("Adds a user with a random ID", async () => {

    data = {  
      "name": "TestUser",  
      "likedEvents": [    "Football Drop-in",    "Jerico Beach Biking",    "21 Savage Concert"  ]
    }
    console.log("BEFORE CALL");
    const response = await (await request(app).post("/users/").send(data));
    console.log(response.body);
    expect(response.status).toBe(200);
  });
});

describe("GET /users/4321", () => {
  test("Return one user with userID 1234", async () => {

    console.log("BEFORE CALL");
    const response = await request(app).get("/users/4321");
    console.log(response.body);
    expect(response.status).toBe(200);
  });
});

describe("PUT /users/4321", () => {
  test("Modifies a user with ID 4321", async () => {

    data = {  
      "name": "Drake",  
      "likedEvents": [    "Football Drop-in",    "Jerico Beach Biking",    "21 Savage Concert"  ]
    }
    console.log("BEFORE CALL");
    const response = await (await request(app).put("/users/4321").send(data));
    console.log(response.body);
    expect(response.status).toBe(200);
  });
});


describe("DELETE /users/4321", () => {
  test("Return one user with userID 4321", async () => {

    console.log("BEFORE CALL");
    const response = await request(app).delete("/users/4321");
    expect(response.status).toBe(200);
  });
});


//**********Liked-Events Tests***************//

describe("GET /:UserId ", () => {
  test("Should return liked events of the user", async () => {
    
    data = 
    {
      "likedEvents": [
        "Event1",
        "Event2"
      ]
    }

    data_expected = [
      "Event1",
      "Event2"
    ]

    axios.get.mockResolvedValue(
      {data,
        status: 200}
    );

    console.log("BEFORE CALL");
    const response = await request(app).get("/liked-events/1001").send();
    // console.log(response);
    expect(response.body).toEqual(data_expected);
    expect(response.status).toBe(200);
  });
});

describe("GET /:UserId ", () => {
  test("Should throw error due to incorrect data", async () => {
    
    data_mock = 
    {}

    axios.get.mockResolvedValue(
      data_mock
    );

    console.log("BEFORE CALL");
    const response = await request(app).get("/liked-events/1001").send();
    expect(response.status).toBe(400);
  });
});

describe("POST /:UserId/:EventID", () => {
  test("Adds an event to liked events list of the user", async () => {
    
    data = {
      id: 1002,
      name: 'Jack',
      likedEvents: [
        "Jerico Beach Biking",
        "21 Savage Concert"
      ]
    }

    data_expected = [
      "Jerico Beach Biking",
      "21 Savage Concert",
      "event3"
    ]

    axios.get.mockResolvedValue(
      {data,
        status: 200}
    );

    axios.put.mockResolvedValue(
      {data:"User updated successfully",
        status: 200}
    );

    console.log("BEFORE CALL");
    const response = await request(app).post("/liked-events/1001/event3").send();
    // console.log(response);
    expect(response.body).toEqual(data_expected);
    expect(response.status).toBe(200);
  });
});

describe("POST /:UserId/:EventID", () => {
  test("Adds an event to liked events list of the user expects error", async () => {
    
    data_err = {
      
    }

    axios.get.mockResolvedValue(
      {data_err,
        status: 200}
    );

    axios.put.mockResolvedValue(
      {data:"User updated successfully",
        status: 200}
    );

    console.log("BEFORE CALL");
    const response = await request(app).post("/liked-events/1002/event3").send();
    expect(response.status).toBe(400);
  });
});

describe("DELETE /:UserId/:EventID", () => {
  test("Deletes an event from liked events list of the user", async () => {
    
    data = {
      id: 1002,
      name: 'Jack',
      likedEvents: [
        "Jerico Beach Biking",
        "21 Savage Concert",
        "event3"
      ]
    }

    data_expected = [
      "Jerico Beach Biking",
      "21 Savage Concert"
    ]

    axios.get.mockResolvedValue(
      {data,
        status: 200}
    );

    axios.put.mockResolvedValue(
      {data:"User updated successfully",
        status: 200}
    );

    console.log("BEFORE CALL");
    const response = await request(app).delete("/liked-events/1002/event3").send();
    // console.log(response);
    expect(response.body).toEqual(data_expected);
    expect(response.status).toBe(200);
  });
});

describe("DELETE /:UserId/:EventID", () => {
  test("Deletes an event from liked events list of the user expects error", async () => {
    
    data_err = {

    }

    axios.get.mockResolvedValue(
      {data_err,
        status: 200}
    );

    axios.put.mockResolvedValue(
      {data:"User updated successfully",
        status: 200}
    );

    console.log("BEFORE CALL");
    const response = await request(app).delete("/liked-events/1002/event3").send();
    // console.log(response);
    expect(response.status).toBe(400);
  });
});

//**********Liked-Locations Tests***************//

describe("GET liked-locations/:UserId ", () => {
  test("Should return liked locations of the user", async () => {
    
    data = 
    {
      "likedLocations": [
        "Loc1",
        "Loc2"
      ]
    }

    data_expected = [
      "Loc1",
      "Loc2"
    ]

    axios.get.mockResolvedValue(
      {data,
        status: 200}
    );

    console.log("BEFORE CALL");
    const response = await request(app).get("/liked-locations/1001").send();
    // console.log(response);
    expect(response.body).toEqual(data_expected);
    expect(response.status).toBe(200);
  });
});

describe("GET liked-locations/:UserId ", () => {
  test("Should throw error due to incorrect data", async () => {
    
    data_mock = 
    {}

    axios.get.mockResolvedValue(
      data_mock
    );

    console.log("BEFORE CALL");
    const response = await request(app).get("/liked-locations/1001").send();
    expect(response.status).toBe(400);
  });
});

describe("POST liked-locations/:UserId/:LocationID", () => {
  test("Adds an event to liked events list of the user", async () => {
    
    data = {
      id: 1002,
      name: 'Jack',
      likedLocations: [
        "Loc1",
        "Loc2"
      ]
    }

    data_expected = [
      "Loc1",
      "Loc2",
      "loc3"
    ]

    axios.get.mockResolvedValue(
      {data,
        status: 200}
    );

    axios.put.mockResolvedValue(
      {data:"User updated successfully",
        status: 200}
    );

    console.log("BEFORE CALL");
    const response = await request(app).post("/liked-locations/1002/loc3").send();
    // console.log(response);
    expect(response.body).toEqual(data_expected);
    expect(response.status).toBe(200);
  });
});

describe("POST liked-locations/:UserId/:LocationID", () => {
  test("Adds an event to liked events list of the user expects error", async () => {
    
    data_err = {
      
    }

    axios.get.mockResolvedValue(
      {data_err,
        status: 200}
    );

    axios.put.mockResolvedValue(
      {data:"User updated successfully",
        status: 200}
    );

    console.log("BEFORE CALL");
    const response = await request(app).post("/liked-locations/1002/event3").send();
    expect(response.status).toBe(400);
  });
});


describe("DELETE liked-locations/:UserId/:LocationID", () => {
  test("Deletes an event from liked events list of the user", async () => {
    
    data = {
      id: 1002,
      name: 'Jack',
      likedLocations: [
        "Loc1",
        "Loc2",
        "loc3"
      ]
    }

    data_expected = [
      "Loc1",
      "Loc2"
    ]

    axios.get.mockResolvedValue(
      {data,
        status: 200}
    );

    axios.put.mockResolvedValue(
      {data:"User updated successfully",
        status: 200}
    );

    console.log("BEFORE CALL");
    const response = await request(app).delete("/liked-locations/1002/loc3").send();
    // console.log(response);
    expect(response.body).toEqual(data_expected);
    expect(response.status).toBe(200);
  });
});

describe("DELETE /:UserId/:LocationID", () => {
  test("Deletes an event from liked events list of the user expects error", async () => {
    
    data_err = {

    }

    axios.get.mockResolvedValue(
      {data_err,
        status: 200}
    );

    axios.put.mockResolvedValue(
      {data:"User updated successfully",
        status: 200}
    );

    console.log("BEFORE CALL");
    const response = await request(app).delete("/liked-locations/1002/event3").send();
    // console.log(response);
    expect(response.status).toBe(400);
  });
});

//**********UI-Preferences Tests***************//

describe("GET ui-preference/:UserId ", () => {
  test("Get UI preferences", async () => {
    
    data = 
    {"UIPreferences":{
      "component1":{
        "URL": "http://abcd123.ca"
      },
      "component2":{
        "length": "10"
      },
      "color1" : "blue"} 
    }

    data_expected = 
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

    console.log("BEFORE CALL");
    const response = await request(app).get("/ui-preference/1001").send();
    // console.log(response);
    expect(response.body).toEqual(data_expected);
    expect(response.status).toBe(200);
  });
});

describe("GET ui-preference/:UserId ", () => {
  test("Should throw error due to incorrect data", async () => {
    
    data_mock = 
    {}

    axios.get.mockResolvedValue(
      data_mock
    );

    console.log("BEFORE CALL");
    const response = await request(app).get("/ui-preference/1001").send();
    expect(response.status).toBe(400);
  });
});

describe("PUT ui-preference/:UserId", () => {
  test("Update UI preferences", async () => {
    
    data = {
      "component1":{
        "URL": "http://abcd123.ca"
      },
      "component2":{
        "length": "10"
      },
      "color1" : "red", 
    }

    data_expected = {
      "component1":{
        "URL": "http://abcd123.ca"
      },
      "component2":{
        "length": "10"
      },
      "color1" : "red", 
    }

    axios.put.mockResolvedValue(
      {data:"User updated successfully",
        status: 200}
    );

    console.log("BEFORE CALL");
    const response = await request(app).put("/ui-preference/1002").send(data);
    // console.log(response);
    expect(response.body).toEqual(data_expected);
    expect(response.status).toBe(200);
  });
});

describe("PUT ui-preference/:UserId", () => {
  test("Update UI preferences: expects error", async () => {
    
    data_err = {
      
    }

    axios.put.mockImplementation(() => {
      throw new Error();
    });

    console.log("BEFORE CALL");
    const response = await request(app).put("/ui-preference/1002").send(data_err);
    expect(response.status).toBe(400);
  });
});


