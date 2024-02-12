let stompClient = null;
const serverWsUrl = 'http://localhost:8081/ws';
const serverHttpUrl = 'http://localhost:8081/api/chat/previous';

function connect() {
    const accessToken = document.getElementById('accessToken').value;
    const matchingId = document.getElementById('matchingId').value;
    const headers = {
        'Authorization': 'Bearer ' + accessToken,
        'matchingId': matchingId,
        'connectType': 'room'
    };

    const socket = new SockJS(serverWsUrl);
    stompClient = Stomp.over(socket);

    stompClient.connect(headers, function(frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe(`/topic/${matchingId}`, function(messageOutput) {
            showMessageOutput(JSON.parse(messageOutput.body));
            markMessageAsRead(matchingId);
        });
        fetchPreviousMessages(matchingId, accessToken);
    }, function(error) {
        console.log('Connection error: ' + error);
    });
}

function fetchPreviousMessages(matchingId, accessToken) {
    const apiUrl = `${serverHttpUrl}/${matchingId}`; // 이전 채팅 기록을 불러오는 서버의 API 주소
    fetch(apiUrl, {
        method: 'GET',
        headers: {
            'Authorization': `Bearer ${accessToken}`,
        }
    })
        .then(response => response.json())
        .then(data => {
            data.response.forEach(messageOutput => {
                showMessageOutput(messageOutput);
                markMessageAsRead(matchingId);
            });
        })
        .catch(error => console.error('Error fetching previous messages:', error));
}

function disconnect() {
    if(stompClient !== null) {
        stompClient.disconnect();
    }
    console.log("Disconnected");
}

function sendMessage() {
    const matchingId = document.getElementById('matchingId').value;
    const message = document.getElementById('messageInput').value;
    const sender = 'User' + Math.floor(Math.random() * 1000); // 예시로 랜덤 사용자 이름 생성
    stompClient.send(`/app/chat/${matchingId}`, {}, JSON.stringify({'content': message}));
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

function markMessageAsRead(matchingId) { // 마지막으로 메세지 읽은 시간 전송
    const seoulTime = new Intl.DateTimeFormat('ko-KR', {
            timeZone: 'Asia/Seoul',
            year: 'numeric',
            month: '2-digit',
            day: '2-digit',
            hour: '2-digit',
            minute: '2-digit',
            second: '2-digit',
            hour12: false
        }).format(new Date());

    stompClient.send(`/app/readMessage/${matchingId}`, {}, JSON.stringify({
        readTime: seoulTime
    }));
}
