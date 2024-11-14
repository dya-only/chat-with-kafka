<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Chat with Kafka</title>
<style>
body {
	margin: 0;
	overflow-x: hidden;
	width: 100vw;
	display: flex;
	flex-direction: column;
	align-items: center;
	font-family: "Noto Sans KR", sans-serif;
}

nav {
	width: 100vw;
	padding: 40px;
	height: 10px;
	box-shadow: 0px 0px 36px -3px rgba(0, 0, 0, 0.1);
	border-bottom: solid 1px rgb(240, 240, 240);
	display: flex;
	justify-content: center;
	align-items: center;
}

.nav-container > img {
	width: 100px;
}

.nav-container {
	width: 70vw;
}

#chatBox {
	width: 70vw;
	height: 70vh;
	overflow-y: scroll;
	box-sizing: border-box;
	padding: 10px
}

#messageInput {
	width: 85%;
	padding: 20px 15px;
	border-radius: 20px;
	border: solid 2px rgb(240, 240, 240);
	background: rgb(250, 250, 250);
	box-shadow: 0px 0px 18px -3px rgba(0, 0, 0, 0.1);
	outline: none;
	margin-right: 30px;
}

#sendButton {
	width: 50px;
	height: 50px;
	min-width: 50px;
	border-radius: 60%;
	border: none;
	background: rgb(182, 16, 255);
	background: linear-gradient(90deg, rgba(182, 16, 255, 1) 0%,
		rgba(160, 0, 255, 1) 100%);
	padding: 10px;
	display: flex;
	justify-content: center;
	align-items: center;
}

.send {
	width: 20px;
	height: 20px;
	margin-right: 2px;
	filter: invert(100%) sepia(97%) saturate(0%) hue-rotate(24deg) brightness(103%) contrast(106%);
}

.message {
	display: flex;
	align-items: center;
	margin-bottom: 10px;
	padding: 10px 0;
}

.profile-img {
	width: 40px;
	height: 40px;
	border-radius: 50%;
	margin-right: 20px;
}

.message-content {
	/* background-color: #f1f1f1; */
	border-radius: 5px;
	max-width: 80vw;
}

.system-message {
	color: gray;
	margin-bottom: 10px;
}

.nickname {
	font-weight: bold;
	margin-bottom: 2px;
}

.sendcontainer {
	width: 72vw;
	padding: 20px;
	display: flex;
	justify-content: center;
	align-items: center
}
</style>

<script
	src="https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js"></script>
<script
	src="https://cdn.jsdelivr.net/npm/stompjs@2.3.3/lib/stomp.min.js"></script>
<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link
	href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@100..900&family=Sacramento&display=swap"
	rel="stylesheet">
</head>
<body>
	<nav>
		<div class="nav-container"><img
			src="https://static-00.iconduck.com/assets.00/kafka-icon-2048x935-cvu4503l.png" /></div>
	</nav>

	<div id="chatBox"></div>
	<div class="sendcontainer">
		<input type="text" id="messageInput" placeholder="메시지를 입력하세요">
		<button id="sendButton">
			<img class="send" src="./send.svg" />
		</button>
	</div>

	<script>
	var wsUrl = "http://localhost:8080/chat";
    var stompClient = null;

    var senderList = ["박성민", "변예현", "김정윤", "킹유진승", "유진우", "최성욱", "손보석", "성홍제", "김승환", "임상현", "김동영", "김신우"];
    var sender = senderList[Math.floor(Math.random() * senderList.length)];
    var imageType = Math.floor(Math.random() * 3) + 1; // 1 ~ 3

        var imageUrls = {
            1: "https://via.placeholder.com/40/FF0000/FFFFFF", 
            2: "https://via.placeholder.com/40/00FF00/FFFFFF",
            3: "https://via.placeholder.com/40/0000FF/FFFFFF",
        };

        window.onload = function() {
            connectWebSocket();
            fetchRecentMessages();
            appendSystemMessage("당신의 사용자명: " + sender);
        };

     	// WebSocket 연결 함수
        function connectWebSocket() {

            var socket = new SockJS(wsUrl);
            stompClient = Stomp.over(socket);
            stompClient.connect({}, function(frame) {

                stompClient.subscribe('/topic/public', function(messageOutput) {
                    var message = JSON.parse(messageOutput.body);
                    appendChatMessage(message.sender, message.content, message.imageType);
                });
            }, function(error) {
                appendSystemMessage("에러가 발생했습니다: " + error);
                console.error("WebSocket 에러:", error);
            });
        }

        function fetchRecentMessages() {
            fetch('http://localhost:8080/messages/recent')
                .then(response => response.json())
                .then(data => {
                    var chatBox = document.getElementById("chatBox");
                    chatBox.innerHTML = "";

                    data.forEach(message => {
                        appendChatMessage(message.sender, message.content, message.imageType);
                    });
                })
                .catch(error => {
                    console.error('Error fetching recent messages:', error);
                });
        }

        function sendMessage() {
            var message = document.getElementById("messageInput").value.trim();
            if (!message) {
                alert("메시지를 입력하세요.");
                return;
            }
            if (stompClient && stompClient.connected) {
                var messageObj = {
                    sender: sender,
                    content: message,
                    imageType: imageType
                };
                stompClient.send("/app/chat", {}, JSON.stringify(messageObj));
                document.getElementById("messageInput").value = "";
            } else {
                alert("서버와의 연결이 끊어졌습니다.");
            }
        }

        function appendSystemMessage(message) {
            var chatBox = document.getElementById("chatBox");
            var messageElement = document.createElement("div");
            messageElement.className = "system-message";
            messageElement.textContent = message;
            chatBox.appendChild(messageElement);
            chatBox.scrollTop = chatBox.scrollHeight;
        }

        function appendChatMessage(senderName, content, imgType) {
            var chatBox = document.getElementById("chatBox");
            var messageElement = document.createElement("div");
            messageElement.className = "message";

            var imgElement = document.createElement("img");
            imgElement.className = "profile-img";
            imgElement.src = imageUrls[imgType] || "https://via.placeholder.com/40?text=Default";

            var messageContent = document.createElement("div");

            var nicknameElement = document.createElement("div");
            nicknameElement.className = "nickname";
            nicknameElement.textContent = senderName;

            var contentElement = document.createElement("div");
            contentElement.className = "message-content";
            contentElement.textContent = content;

            messageContent.appendChild(nicknameElement);
            messageContent.appendChild(contentElement);

            messageElement.appendChild(imgElement);
            messageElement.appendChild(messageContent);
            chatBox.appendChild(messageElement);
            chatBox.scrollTop = chatBox.scrollHeight;
        }


        document.getElementById("sendButton").onclick = function() {
            sendMessage();
        };
        
        document.getElementById("messageInput").onkeypress = function(event) {
       
        	
            if (event.key === "Enter") {
                sendMessage();
            }
        };
    </script>
</body>
</html>