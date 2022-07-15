const chatForm = document.getElementById('chat-form')
const chatMessages = document.querySelector('.chat-messages')
const roomName = document.getElementById('room-name')
const userList = document.getElementById('users')



// Get username and room from URL
const {username, room} = Qs.parse(location.search,{
    ignoreQueryPrefix: true
});

console.log(username, room);

const socket = io();

// Join chatroom:
socket.emit('connectUser', username)

setTimeout(()=>{
    socket.emit('getChatHistory', '1')
    socket.on('output1', (res)=>{
        console.log(res);
    })
}, 1000)

setTimeout(()=>{
    socket.emit('joinRoom', '2')
}, 2000)


// socket.emit('joinRoom', {username, room})

// Get room info and users:
socket.on('roomUsers',({room, users})=>{
    outputRoomName(room);
    outputUsers(users);
} )

// message from server
socket.on('message', message => {
    console.log(message);
    outputMessage(message);

    // scroll down
    chatMessages.scrollTop = chatMessages.scrollHeight;
});

chatForm.addEventListener('submit', (e)=>{
    e.preventDefault();  

    // get message text
    const msg = e.target.elements.msg.value;

    // emit a message to the server
    socket.emit('chatMessage', {eventID:'1',msg});

    // clear input
    e.target.elements.msg.value = '';
    e.target.elements.msg.focus();
})

// output message to DOM
function outputMessage(message){
    console.log("received message from"+message.username)
    const div = document.createElement('div');
    div.classList.add('message');
    div.innerHTML = `
    <p class="meta">${message.username} <span>${message.time}</span></p>
    <p class="text">
        ${message.text}
    </p>
    `
    document.querySelector('.chat-messages').appendChild(div);
}

// add room name to DOM
function outputRoomName(room){
    roomName.innerText = room;
}

// Add users to DOM:
function outputUsers(users){
    userList.innerHTML =
    `
        ${users.map(user => `<li>${user.username}</li>`).join('')}
    `
}