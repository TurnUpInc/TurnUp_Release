const request = require("supertest");
// const app = require("../../EventManager/eventManager.js");
const app = require("./NewEventManager/eventManager.js");
var test_e_id;

describe("GET(‘events/:eventId’)", () => {
  it("Get event with e_id present in the DB", async () => {
    e_id = "62d0b71aeeb2766fce8d7f1c";
    const response = await request(app)
      .get("/events/" + e_id)
      .expect(200)
      expect(response.body.e_id).toBe(e_id);
  });

  it("Get event with e_id not present in the DB", async () => {
    e_id = "4444b71aeeb2766fce8d7f1c";
    const response = await request(app)
      .get("/events/" + e_id)
      .expect(404);
    expect(response.body).toEqual({});
  });
});

describe("GET(‘events’)", () => {
  it("Get all events, assumes at least one event in DB", async () => {
    const response = await request(app).get("/events").expect(200);
    expect(response.body.length).toBeGreaterThan(0);
  });
});

describe("POST(‘events/:UserID’)", () => {
  it("Create new event with creator set to UserID", async () => {
    userId = "newUser5";
    payload = {
      name: "New Event 5",
      rating: "4",
    };
    const response = await request(app)
      .post("/events/" + userId)
      .send(payload)
      .expect(200);
    expect(response.text).toBeDefined();
    expect(typeof response.text).toBe("string");
    test_e_id = response.text;
  });
  it("Create new event with creator set to UserID, userID empty", async () => {
    userId = "";
    payload = {
      name: "New Event 5",
      rating: "4",
    };
    const response = await request(app)
      .post("/events/" + userId)
      .send(payload)
      .expect(404);
    expect(response.text).toBeDefined();
    expect(typeof response.text).toBe("string");
  });
});

describe("DELETE(‘events/:EventID/:UserID)", () => {
  it("Delete event with e_id, UserID", async () => {
    userId = "newUser5";
    const response = await request(app)
      .delete("/events/" + test_e_id + "/" + userId)
      .expect(200);
    expect(response.text).toEqual("Event deleted successfully");
  });

  it("Delete event but incorrect EventID", async () => {
    userId = "dneUser";
    e_id = "123";
    const response = await request(app)
      .delete("/events/" + e_id + "/" + userId)
      .expect(404);
    expect(response.text).toEqual("Event not found");
  });
  it("Delete event but incorrect Creator", async () => {
    userId = "dneUser";
    e_id = "62e4917c4ba25d7268b3b809";
    const response = await request(app)
      .delete("/events/" + e_id + "/" + userId)
      .expect(400);
    expect(response.text).toEqual("You are not the creator");
  });
});

describe("PUT(‘events/:EventID/:UserID’)", () => {
  it("Update event with e_id, UserID", async () => {
    userId = "test3";
    e_id = "62d0b784eeb2766fce8d7f1e";
    payload = {
      name: "New Event 3",
      rating: "4",
    };
    const response = await request(app)
      .put("/events/" + e_id + "/" + userId)
      .send(payload)
      .expect(200);
    expect(response.text).toEqual("Event modified successfully\n");
  });

  it("Update event but incorrect UserID or EventID", async () => {
    userId = "dneUser";
    e_id = "123";
    payload = {
      name: "New Event 3",
      rating: "4",
    };
    const response = await request(app)
      .put("/events/" + e_id + "/" + userId)
      .send(payload)
      .expect(400);
    expect(response.text).toEqual("Event not found or you are not the creator");
  });
});

describe("GET(‘events-by-category’)", () => {
  it("Get event with category present in the DB", async () => {
    category = "funny";
    const response = await request(app)
      .get("/events-by-category?cat=" + category)
      .expect(200);
    expect(response.body.length).toBeGreaterThan(0);
  });

  it("Get event with category not present in the DB", async () => {
    category = "dne";
    const response = await request(app)
      .get("/events-by-category?cat=" + category)
      .expect(200);
    expect(response.body.length).toEqual(0);
  });
});

describe("GET(‘events-by-rating’)", () => {
  it("Get all events present in DB in descending order by rating", async () => {
    const response = await request(app)
    .get("/events-by-rating")
    .expect(200);
    flag = true;
    for (let i = 0; i < response.body.length - 1; i++) {
      if (response.body[i].rating < response.body[i + 1].rating) {
        flag = false;
      }
    }
    expect(flag).toBe(true);
  });
});
