package com.poolingpeople.talks.boundary;

import com.poolingpeople.talks.entities.WSMessage;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonString;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import java.io.StringReader;

/**
 * Created by alacambra on 1/6/15.
 */
public class ChatDecoder implements Decoder.Text<WSMessage> {
    @Override
    public WSMessage decode(String s) throws DecodeException {
        System.out.println(s);
        JsonObject jsonObject = Json.createReader(new StringReader(s)).readObject();
        return new WSMessage(
                ((JsonString)jsonObject.get("type")).getString(),
                ((JsonString)jsonObject.get("msg")).getString()
        );
    }

    @Override
    public boolean willDecode(String s) {
        return true;
    }

    @Override
    public void init(EndpointConfig config) {

    }

    @Override
    public void destroy() {

    }
}
