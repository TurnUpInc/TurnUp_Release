const request = require("supertest");
const app = require("./EventManagerTest");

//**********EventStore and EventFilter Tests***************//

describe("POST /events/", () => {
  test("Posts a new event by User 4321", async () => {

    data = {  
        "_id":"1234",
        "name": "Football Drop-in",  
        "category":"sports",
        "rating":"3"
    }

    const response = await (await request(app).post("/events/4321").send(data));
    // console.log(response.body);
    expect(response.status).toBe(200);
  });
});

describe("GET /events/", () => {
    test("Gets all the events", async () => {

        data=[
            {
              _id: '1234',
              name: 'Football Drop-in',
              category: 'sports',
              rating: '3',
              Creator: '4321',
              e_id: '1234'
            }
          ]
      const response = await (await request(app).get("/events/"));
    //   console.log(response.body);
      expect(response.body).toStrictEqual(data)
      expect(response.status).toBe(200);
    });
  });

describe("GET /events/1234", () => {
test("Should return list of all the users", async () => {

    data=
        {
          _id: '1234',
          name: 'Football Drop-in',
          category: 'sports',
          rating: '3',
          Creator: '4321',
          e_id: '1234'
        }
      

    const response = await (await request(app).get("/events/1234"));
    // console.log(response.body);
    expect(response.body).toStrictEqual(data)
    expect(response.status).toBe(200);
});
});

describe("GET /events-by-category", () => {
    test("Should return list of all the users", async () => {
        data=[
            {
              _id: '1234',
              name: 'Football Drop-in',
              category: 'sports',
              rating: '3',
              Creator: '4321',
              e_id: '1234'
            }
          ]
  
        const response = await (await request(app).get("/events-by-category/").query({ cat: 'sports' }));
        expect(response.body).toStrictEqual(data)
        expect(response.status).toBe(200);
    });
    });

describe("GET /events-by-rating", () => {
    test("Should return list of all the users", async () => {
        data=[
            {
                _id: '1234',
                name: 'Football Drop-in',
                category: 'sports',
                rating: '3',
                Creator: '4321',
                e_id: '1234'
            }
            ]
  
        const response = await (await request(app).get("/events-by-rating/"));
        expect(response.body).toStrictEqual(data)
        expect(response.status).toBe(200);
    });
    });

describe("PUT /events/1234/4321", () => {
test("Should return list of all the users: Throws Error", async () => {

    data = {  
        "_id":"1234",
        "name": "Football Drop-in",  
        "category":"sports",
        "rating":"3"
    }


    const response = await (await request(app).put("/events/1234/xyx").send(data));
    // console.log(response.body);
    expect(response.status).toBe(400);
});
});

describe("PUT /events/1234/4321", () => {
    test("Should return list of all the users", async () => {
    
        data = {  
            "_id":"1234",
            "name": "Football Drop-in",  
            "category":"sports",
            "rating":"3"
        }
    
    
        const response = await (await request(app).put("/events/1234/4321").send(data));
        // console.log(response.body);
        expect(response.status).toBe(200);
    });
    });

describe("DELETE /events/1234/4321", () => {
    test("Deletes event 1234, throws Error", async () => {
  
      const response = await (await request(app).delete("/events/1234/xyz"));
      expect(response.status).toBe(400);
    });
  });

  describe("DELETE /events/1234/4321", () => {
    test("Deletes event 1234", async () => {
  
      const response = await (await request(app).delete("/events/1234/4321"));
      expect(response.status).toBe(200);
    });
  });