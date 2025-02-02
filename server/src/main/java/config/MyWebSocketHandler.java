package config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MyWebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final CopyOnWriteArrayList<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        System.out.println("Connected:" + session.getId());
        Message serverResponse = new Message(session.getId() + " has joined the chat");
        String jsonResponse = objectMapper.writeValueAsString(serverResponse);


        broadcastMessage(jsonResponse);
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        String messageContent = message.getPayload().toString();
        System.out.println("Received Message: " + messageContent);
        try {
            Message messageObject = objectMapper.readValue(messageContent, Message.class );
            Message serverResponse = new Message(messageObject.getRecipientId().toString() + messageObject.getContent());
            String recipientId = messageObject.getRecipientId();


            String jsonResponse = objectMapper.writeValueAsString(serverResponse);

            if (recipientId == null || recipientId.isBlank()) {
                broadcastMessage(jsonResponse);
            }   else {
                sendToRecipient(recipientId, jsonResponse);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendToRecipient(String recipientId, String message) throws IOException {
        for (WebSocketSession session : sessions) {
            if (session.isOpen() && session.getId().equals(recipientId)) {
                session.sendMessage(new TextMessage(message));
                break;
            }
        }
    }


    private void broadcastMessage(String message) throws IOException {
        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                session.sendMessage(new TextMessage(message));
            }
        }
    }

}
