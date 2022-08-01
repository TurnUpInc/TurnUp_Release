const request = require("supertest");
// const app = require("../../EventManager/eventManager.js");
const app = require("./NewLocationManager/locationServer.js");
var test_e_id;

describe("POST(‘locations/:UserID’)", () => {
  it("Create new loc with creator set to UserID", async () => {
    userId = "newUser5";
    payload = {
      name: "New Location 5",
      userId: "newUser5",
      rating: "4",
      l_id: "test",
    };
    const response = await request(app)
      .post("/locations/" + userId)
      .send(payload)
      .expect(200);
    expect(response.text).toBeDefined();
    expect(typeof response.text).toBe("string");
    test_e_id = response.text;
  });
});


describe("GET(‘locations/:locationId’)", () => {
    it("Get event with l_id present in the DB", async () => {
      l_id = "test";
      const response = await request(app)
        .get("/locations/" + l_id)
        .expect(200);
      expect(response.body).toBeDefined();
    });
  
    it("Get location with l_id not present in the DB", async () => {
      l_id = "4444b71aeeb2766fce8d7f1c";
      const response = await request(app)
        .get("/locations/" + l_id)
        .expect(404);
      expect(response.body).toEqual({});
    });
  });
  
  describe("GET(‘locations’)", () => {
    it("Get all locations", async () => {
      const response = await request(app).get("/locations").expect(200);
      expect(response.body.length).toBeGreaterThan(0);
    });
  });

  describe("PUT(‘locations/:LocationID’)", () => {
    it("Update loc with l_id", async () => {
      payload = {
        name: "New Loc 3",
        rating: "4",
        l_id: "test"
      };
      const response = await request(app)
        .put("/locations/test")
        .send(payload)
        .expect(200);
      expect(response.text).toEqual("Location modified successfully\n");
    });
  
    it("Update event but incorrect LocationID", async () => {
      payload = {
        name: "New 3",
        rating: "4",
      };
      const response = await request(app)
        .put("/locations/dne")
        .send(payload)
        .expect(404);
      expect(response.text).toEqual("Location not found\n");
    });
  });
  

describe("DELETE(‘location/:LocationID/)", () => {
  it("Delete loc with l_id", async () => {
    const response = await request(app).delete("/locations/test").expect(200);
    expect(response.text).toEqual("Todo item deleted successfully\n");
  });

  it("Delete loc but incorrect LocationID", async () => {
    const response = await request(app)
      .delete("/locations/wrongtest")
      .expect(404);
    expect(response.text).toEqual("Location not found\n");
  });
});

describe("GET(‘locations-by-cooridnates)", () => {
    it("With coordinates not present", async () => {
        const response = await request(app).get("/locations-by-cooridnates").expect(404);
    });
    // it ("With valid coordiantes", async () => {
    //     const response = await request(app).get("/locations-by-cooridnates?latitude1=40&longitude1=74&latitude2=100&longitude2=100").expect(200);
    // });
});

describe("put(incrlike/:locationID)", () =>{
    it("valid locID", async () => {
        const response = await request(app).put("/incrlike/test2").expect(200);
        expect(response.text).toEqual("Location Liked!\n");
    });
    it("invalid locID", async () => {
        const response = await request(app).put("/incrlike/dbe").expect(404);
        expect(response.text).toEqual("Location not found");
    });
});

describe("put(incrvisitors/:locationID)", () =>{
    it("valid locID", async () => {
        const response = await request(app).put("/incrvisitors/test2").expect(200);
        expect(response.text).toEqual("Location Visited!\n");
    });
    it("invalid locID", async () => {
        const response = await request(app).put("/incrvisitors/dbe").expect(404);
        expect(response.text).toEqual("Location not found");
    });
});

