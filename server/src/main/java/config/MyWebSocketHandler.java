package config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class MyWebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("Connected:" + session.getId());
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        String messageContent = message.getPayload().toString();
        System.out.println("Received Message: " + messageContent);

        Message serverResponse = new Message("Hello from the server");

        String jsonResponse = objectMapper.writeValueAsString(serverResponse);

        session.sendMessage(new TextMessage(jsonResponse));

        try {

            Message messageObject = objectMapper.readValue(messageContent, Message.class );
            System.out.println(messageObject.getRecipientId());
            System.out.println(messageObject.getContent());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
