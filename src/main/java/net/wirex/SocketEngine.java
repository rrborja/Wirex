/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.wirex;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.Map;
import net.wirex.structures.XLive;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author ritchie
 */
public final class SocketEngine {

    private static final Logger LOG = LoggerFactory.getLogger(SocketEngine.class.getSimpleName());

    private WebSocketClient con;

    private static class SingletonHolder {

        public static SocketEngine INSTANCE = new SocketEngine();

    }

    private SocketEngine() {
        try {
            //URI uri = new URI(hostname.replace("http", "ws") + "wired/");
            URI uri = new URI("ws://localhost:8080/wired");
            con = new WebSocketClient(uri, new Draft_17()) {

                @Override
                public void onOpen(ServerHandshake sh) {
                    LOG.info("Wirex is communicating with hosting server.");
                }

                @Override
                public void onMessage(String string) {
                    if (string.equals("G7")) {
                        LOG.info("Real-time collaboration server is connected!");
                        return;
                    }
                    ResponseStructure response = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").registerTypeAdapter(Date.class, new DateJsonDeserializer()).create().fromJson(string, ResponseStructure.class);
                    String feature = response.getFeature();
                    XLive component = AppEngine.releaseXLive(feature);
                    Map map = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").registerTypeAdapter(Date.class, new DateJsonDeserializer()).create().fromJson(String.valueOf(response.getBody()), new TypeToken<Map<String, String>>() {
                    }.getType());
                    if (component == null) {
                        System.out.println(string);
                    } else {
                        component.onChanges(map);
                    }
                }

                @Override
                public void onClose(int i, String string, boolean bln) {
                    LOG.info("Wirex connection is closing.");
                }

                @Override
                public void onError(Exception excptn) {
                    excptn.printStackTrace();
                    LOG.warn("Server was disconnected. Wirex encountered a problem");
                }
            };
            con.connect();
        } catch (URISyntaxException e) {

        }
    }

    public void disconnect() {
        con.close();
    }

    public static SocketEngine getInstance() {
        return SingletonHolder.INSTANCE;
    }
}
