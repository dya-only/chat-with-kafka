<!DOCTYPE html>
<html>
<head>
    <title>Chat with Kafka</title>
    <script src="https://cdn.jsdelivr.net/npm/sockjs-client/dist/sockjs.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/stompjs/lib/stomp.min.js"></script>
    <script>
        var stompClient = null;

        function connect() {
            var socket = new SockJS('/ws');
            stompClient = Stomp.over(socket);
            stompClient.connect({}, function (frame) {
                console.log('Connected: ' + frame);
                stompClient.subscribe('/topic/public', function (message) {
                    showMessage(JSON.parse(message.body));
                });
            });
        }

        function sendMessage() {
            var messageContent = document.getElementById("message").value;
            if (messageContent && stompClient) {
                var chatMessage = {
                    sender: "사용자 이름",
                    content: messageContent,
                    timestamp: new Date().toISOString()
                };
                stompClient.send("/app/chat.send", {}, JSON.stringify(chatMessage));
                document.getElementById("message").value = '';
            }
        }

        function showMessage(message) {
            var messageElement = document.createElement('li');
            messageElement.appendChild(document.createTextNode(message.sender + ": " + message.content));
            document.getElementById("messageList").appendChild(messageElement);
        }

        window.onload = function() {
            connect();
        };
    </script>
</head>
<body>
    <ul id="messageList"></ul>
    <input type="text" id="message" placeholder="메시지를 입력하세요..." />
    <button onclick="sendMessage()">전송</button>
</body>
</html>
