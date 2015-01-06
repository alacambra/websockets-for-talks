package com.poolingpeople.talks.boundary;

import com.poolingpeople.talks.entities.WSMessage;

import javax.json.Json;
import javax.json.JsonObject;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

/**
 * Created by alacambra on 1/6/15.
 */
public class ChatEncoder implements Encoder.Text<WSMessage>{
    @Override
    public String encode(WSMessage object) throws EncodeException {
        JsonObject jsonObject = Json.createObjectBuilder()
                .add("type", object.getType().name())
                .add("msg",object.getBody())
                .build();
        return jsonObject.toString();
    }

    @Override
    public void init(EndpointConfig config) {

    }

    @Override
    public void destroy() {

    }
}
