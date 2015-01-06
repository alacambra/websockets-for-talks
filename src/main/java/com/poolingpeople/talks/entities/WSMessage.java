package com.poolingpeople.talks.entities;

/**
 * Created by alacambra on 1/6/15.
 */
public class WSMessage {

    public static enum Type{
        connection,
        message
    }

    Type type;
    String body;

    public WSMessage(String type, String body) {
        this.type = Type.valueOf(type);
        this.body = body;
    }

    public Type getType() {
        return type;
    }

    public String getBody() {
        return body;
    }
}
