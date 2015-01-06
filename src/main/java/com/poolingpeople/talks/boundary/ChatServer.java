package com.poolingpeople.talks.boundary;

import com.poolingpeople.talks.controller.MessagePersister;
import com.poolingpeople.talks.entities.TalkLog;
import com.poolingpeople.talks.entities.WSMessage;

import javax.inject.Inject;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;

@ServerEndpoint(value = "/chat/{room}" , encoders = {ChatEncoder.class}, decoders = {ChatDecoder.class})
public class ChatServer {
    private static final Map<Long, Set<Session>> peers = Collections.synchronizedMap(new HashMap<Long, Set<Session>>());

    @Inject
    MessagePersister messagePersister;

    @Inject
    LoggedUser loggedUser;

    @OnOpen
    public void onOpen(Session peer,  @PathParam("room") Long room) throws IOException, EncodeException {
        if (!peers.containsKey(room)){
            peers.put(room, new HashSet<Session>());
        }

        peers.get(room).add(peer);
    }

    @OnClose
    public void onClose(Session peer, @PathParam("room") Long room) {
        peers.get(room).remove(peer);
    }

    @OnMessage
    public void message(WSMessage message, Session client, @PathParam("room") Long room) throws IOException, EncodeException {

        for (Session peer : peers.get(room)) {
            peer.getBasicRemote().sendObject(new WSMessage(message.getType().toString(),
                    message.getType() == WSMessage.Type.connection ? message.getBody() :
                    message.getBody()));
        }

        TalkLog log = new TalkLog();
        log.setAuthor(loggedUser.getUser());
        log.setCreationTime(Calendar.getInstance());
        log.setLog(message.getBody());

        messagePersister.addLog(room, log);
    }
}
