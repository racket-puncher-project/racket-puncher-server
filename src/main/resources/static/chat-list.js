let stompClient = null;
const serverHttpUrl = 'http://localhost:8081/api/chat/list';
const serverWsUrl = 'http://localhost:8081/ws';

function fetchChatRooms() {
    const accessToken = document.getElementById('accessToken').value;
    if (!accessToken) {
        alert('엑세스 토큰을 입력해주세요.');
        return;
    }

    fetch(serverHttpUrl, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${accessToken}`,
            'Content-Type': 'application/json'
        }
    })
        .then(response => response.json())
        .then(data => displayChatRooms(data.response))
        .catch(error => console.error('Error fetching chat rooms:', error));
    connectWebSocket();
}

function displayChatRooms(chatRooms) {
    const container = document.getElementById('chatRooms');
    container.innerHTML = ''; // Clear previous content

    chatRooms.forEach(room => {
        const roomElement = document.createElement('div');
        roomElement.innerHTML = `
            <h3>${room.title}</h3>
            <p>Matching ID: ${room.matchingId}</p>
            <p>New Messages: ${room.newMessageNum}</p>
            <div>Participants:</div>
            ${room.participants.map(participant => `
                <div>
                    <img src="${participant.profileImg}" alt="${participant.nickname}" style="width:50px;height:50px;">
                    <span>${participant.nickname}</span>
                </div>
            `).join('')}
        `;
        container.appendChild(roomElement);
    });
}

function connectWebSocket() {
    const accessToken = document.getElementById('accessToken').value;
    if (!accessToken) {
        alert('엑세스 토큰을 입력해주세요.');
        return;
    }

    const socket = new SockJS(serverWsUrl);

    stompClient = Stomp.over(socket);

    const headers = {
        'Authorization': 'Bearer ' + accessToken,
        'connectType' : 'list'
    };

    stompClient.connect(headers, function(frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/newMessageArrived', function(message) {
            const data = JSON.parse(message.body);
            if (data && data.matchingId) {
                updateNewMessageNum(data.matchingId);
            }
        });
    }, function(error) {
        console.log('STOMP connection error: ' + error);
    });
}

function updateNewMessageNum(matchingId) {
    const chatRooms = document.querySelectorAll('#chatRooms > div');
    chatRooms.forEach(room => {
        const roomId = room.querySelector('p').textContent.split(': ')[1];
        if (roomId === matchingId) {
            const newMessageElement = room.querySelector('p:nth-child(3)');
            let newMessageNum = parseInt(newMessageElement.textContent.split(': ')[1]);
            newMessageElement.textContent = 'New Messages: ' + (newMessageNum + 1);
        }
    });
}
