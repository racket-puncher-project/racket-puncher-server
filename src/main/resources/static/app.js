let stompClient = null;
const serverUrl = 'http://localhost:8081/ws';

function connect() {
    const accessToken = document.getElementById('accessToken').value;
    const headers = {
        'Authorization': 'Bearer ' + accessToken
    };

    const socket = new SockJS(serverUrl);
    stompClient = Stomp.over(socket);

    stompClient.connect(headers, function(frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/messages', function(messageOutput) {
            showMessageOutput(JSON.parse(messageOutput.body));
        })
    }, function(error) {
        console.log('Connection error: ' + error);
    });
}

function disconnect() {
    if(stompClient !== null) {
        stompClient.disconnect();
    }
    console.log("Disconnected");
}

function sendMessage() {
    const message = document.getElementById('messageInput').value;
    const sender = 'User' + Math.floor(Math.random() * 1000); // 예시로 랜덤 사용자 이름 생성
    stompClient.send("/app/chat", {}, JSON.stringify({'content': message}));
}

function showMessageOutput(messageOutput) {
    const response = document.getElementById('response');
    const messageBox = document.createElement('div');
    messageBox.className = 'chat-message';

    const messageInfo = document.createElement('div');
    messageInfo.className = 'message-info';
    const profileImg = document.createElement('img');
    profileImg.src = messageOutput.senderProfileImg;  // 메시지 보낸이의 프로필 이미지 URL
    profileImg.className = 'profile-img';
    const senderNickname = document.createTextNode(messageOutput.senderNickname);
    const sentTime = document.createTextNode(` [${messageOutput.sentTime}]`);

    messageInfo.appendChild(profileImg);
    messageInfo.appendChild(senderNickname);
    messageInfo.appendChild(sentTime);

    const messageText = document.createElement('div');
    messageText.className = 'message-text';
    messageText.appendChild(document.createTextNode(messageOutput.content));

    messageBox.appendChild(messageInfo);
    messageBox.appendChild(messageText);

    response.appendChild(messageBox);
}