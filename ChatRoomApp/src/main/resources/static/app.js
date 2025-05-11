let stompClient = null;
let recipient = null;
let username = null;
const userList = document.querySelector("#users");
const refreshBtn = document.querySelector("#refreshList");
let chatWindowCheck = false;


document.addEventListener("DOMContentLoaded", () => {
    const connectBtn = document.querySelector("#connect");
    const currentUser = document.querySelector("#currUser");
    const messageArea = document.querySelector("#messageArea");
    username = currentUser.value.trim();
    fetchUsers();

    connectBtn.addEventListener("click", connect);

    refreshBtn.addEventListener("click", fetchUsers);

    function fetchUsers() {
        fetch("http://localhost:8080/home/refresh")
            .then(res => res.text())
            .then(html => {
                userList.innerHTML = html;
                attachUserBtns();
            })
            .catch(error=>{
                console.log("fetchUsers error", error);
            })
            }

    setInterval(fetchUsers, 600000);

    function attachUserBtns() {
        const userButtons = document.querySelectorAll(".user-button");
        if (recipient) {
            document.getElementById(recipient).style.backgroundColor = "black";
        }
        userButtons.forEach(button => {
            button.addEventListener("click", () => {
                recipient = button.id;
                button.style.backgroundColor = "black";
                showMessageArea(recipient);
            });
        });
    }

    function showMessageArea(recipient) {
        fetch(`http://localhost:8080/home/message-area?recipient=${recipient}`)
            .then(res => res.text())
            .then(html => {
                messageArea.innerHTML = html;
                chatWindowCheck = true;
                setupChatForm();
            })
            .catch(error=>{
                console.log("messageArea error:", error);
            });
            }


    function setupChatForm() {
        const form = document.getElementById('chatForm');
        const input = document.getElementById('messageInput');
        const closeChatBtn = document.getElementById('cancelChat');
        const messages = document.getElementById('messages');

        console.log("CHATSSS", { form, input, closeChatBtn, messages });

        if (!form || !input || !closeChatBtn || !messages) {
            console.error("One or more elements missing in the chat fragment.");
            return;
        }
        fetchChatHistory(recipient, username);
        closeChatBtn.addEventListener("click", hideMessageArea);


        form.addEventListener('submit', function (e) {
            e.preventDefault();
            const text = input.value.trim();
            if (text) {
                const chatMessage = {
                    sender: username,
                    recipient: recipient,
                    message: text
                };
                const msgDiv = document.createElement('div');
                msgDiv.textContent = `${username}: ${text}`;
                msgDiv.classList.add('mb-2', 'p-2', 'bg-light', 'rounded');
                messages.appendChild(msgDiv);
                messages.scrollTop = messages.scrollHeight;
                input.value = '';
                stompClient.send("/app/chat", {}, JSON.stringify(chatMessage));
            }
        });
    }

    function fetchChatHistory(recipient, username) {
        fetch(`http://localhost:8080/home/getChatHistory?sender=${username}&recipient=${recipient}`)
            .then(res => {
                return res.json()
            })
            .then(userList => {
                console.log(userList);
                userList.forEach(user => {
                    messageFromHistory = document.createElement("div");
                    messageFromHistory.textContent = user;
                    messageFromHistory.classList.add('mb-2', 'p-2', 'bg-light', 'rounded');
                    messages.appendChild(messageFromHistory);
                    messages.scrollTop = messages.scrollHeight;
                })
            })
            .catch(error=>{
                console.log("chatHistory error:", error);
            })

    }

    function hideMessageArea() {
        messageArea.innerHTML = "";
        document.querySelector(`#${recipient}`).style.backgroundColor = "green";
        chatWindowCheck = false;
    }

    function connect() {
        const socket = new SockJS("/ws");
        stompClient = Stomp.over(socket);

        stompClient.connect({}, () => {
            stompClient.subscribe("/user/queue/chat", onMessageReceived);
            console.log("Connected as", username);
        });
    }

    function onMessageReceived(payload) {
        const chatMessage = JSON.parse(payload.body);
        if (!chatWindowCheck) {
            alert(`${chatMessage.sender} sent you a message: ${chatMessage.message}`);
        }
        const messages = document.getElementById("messages");
        if (!messages) return;
        const msgDiv = document.createElement("div");
        msgDiv.textContent = `${chatMessage.sender}: ${chatMessage.message}`;
        msgDiv.classList.add('mb-2', 'p-2', 'bg-light', 'rounded');
        messages.appendChild(msgDiv);
        messages.scrollTop = messages.scrollHeight;
    }

});