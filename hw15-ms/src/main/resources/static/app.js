let stompClient = null;

const createUser = () => {

    stompClient = Stomp.over(new SockJS('/gs-guide-websocket'));
    stompClient.connect({}, (frame) => {
        console.log('Connected: ' + frame);
        stompClient.send("/app/createUser", {}, JSON.stringify({ 'name': $("#name_input").val(), 'age': $("#age_input").val() }));
        stompClient.subscribe('/topic/createUser', window.location.replace("/users"));
    });
};

$(function () {
    $("form").on('submit', (event) => {
        event.preventDefault();
    });
    $("#send").click(createUser);
});
