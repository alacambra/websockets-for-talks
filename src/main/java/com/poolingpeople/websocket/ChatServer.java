/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2013 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package com.poolingpeople.websocket;

import javax.sound.midi.Soundbank;
import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.*;

/**
 * @author Arun Gupta
 */
@ServerEndpoint("/websocket/{room}")
public class ChatServer {
    private static final Map<String, Set<Session>> peers = Collections.synchronizedMap(new HashMap<String, Set<Session>>());

    @OnOpen
    public void onOpen(Session peer,  @PathParam("room") String room) {
        if (!peers.containsKey(room)){
            peers.put(room, Collections.synchronizedSet(new HashSet<Session>()));
        }

        peers.get(room).add(peer);
        System.out.println("init on room " + room + " on " + this.toString() + " with session " + peer );

    }

    @OnClose
    public void onClose(Session peer, @PathParam("room") String room) {
        peers.get(room).remove(peer);
        System.out.println("close" + " on " + this.toString() + " with session " + peer );
    }

    @OnMessage
    public void message(String message, Session client, @PathParam("room") String room) throws IOException, EncodeException {
        System.out.println("message for room " + room + " on " + this.toString() + " with session " + client );
        for (Session peer : peers.get(room)) {
            peer.getBasicRemote().sendObject(message);
        }
    }
}
