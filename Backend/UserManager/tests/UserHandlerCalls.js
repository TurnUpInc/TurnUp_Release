// const userHandler = require('../UserHandler');

const axios = require('axios');

jest.mock('axios');
var baseURL = "localhost:8080"

// User Store

async function getAllUsers() {
    const response = await axios.get(baseURL+'/users/');
    return response;
}

async function getOneUser() {
    const response = await axios.get(baseURL+'/users/1002');
    return response;
}

async function addNewUser() {
    const response = await axios.post(baseURL+'/users/');
    return response;
}

async function updateUser() {
    const response = await axios.put(baseURL+'/users/1002');
    return response;
}


async function deleteUser() {
    const response = await axios.delete(baseURL+'/users/1002');
    return response;
}

// UI Preferences

async function getUIPreferences() {
    const response = await axios.get(baseURL+'/ui-preference/1002');
    return response;
}


async function updateUIPreferences() {
    const response = await axios.put(baseURL+'/ui-preference/1002');
    return response;
}

// Liked Locations

async function getLikedLocations() {
    const response = await axios.get(baseURL+'/liked-locations/1002');
    return response;
}

async function addLikedLocation() {
    const response = await axios.post(baseURL+'/liked-locations/1002/1234123479');
    return response;
}

async function deleteLikedLocation() {
    const response = await axios.delete(baseURL+'/liked-locations/1002/1234123479');
    return response;
}

// Liked Events

async function getLikedEvents() {
    const response = await axios.get(baseURL+'/liked-events/1002');
    return response;
}

async function addLikedEvents() {
    const response = await axios.post(baseURL+'/liked-events/1002/1234123479');
    return response;
}

async function deleteLikedEvents() {
    const response = await axios.delete(baseURL+'/liked-events/1002/1234123479');
    return response;
}


module.exports = {getAllUsers, getOneUser, addNewUser, updateUser, deleteUser,
    getUIPreferences, updateUIPreferences, getLikedLocations, addLikedLocation,
    deleteLikedLocation, getLikedEvents, addLikedEvents, deleteLikedEvents};