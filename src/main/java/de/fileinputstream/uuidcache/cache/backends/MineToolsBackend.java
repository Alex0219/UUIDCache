package de.fileinputstream.uuidcache.cache.backends;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * This class has been generated by Alexander on 29.04.18 22:21
 * You are not allowed to edit this resource or other components of it
 * © 2018 Alexander Fiedler
 */
public class MineToolsBackend {

    public ExecutorService service = Executors.newCachedThreadPool();


    public void getUUID(String username, Consumer<String> consumer) {


        service.execute(() -> {
            try {
                URL url = new URL("https://api.minetools.eu/uuid/" + username);
                final JSONParser parser = new JSONParser();
                try {
                    final JSONObject json = (JSONObject) parser.parse(new InputStreamReader(url.openStream()));
                    String uuid = insertDashUUID(json.get("id").toString());
                    consumer.accept(uuid);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }


            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                consumer.accept("Ein unbekannter Fehler ist aufgetreten.");
            }
        });
    }
    public static String insertDashUUID(String uuid) {
        StringBuffer sb = new StringBuffer(uuid);
        sb.insert(8, "-");

        sb = new StringBuffer(sb.toString());
        sb.insert(13, "-");

        sb = new StringBuffer(sb.toString());
        sb.insert(18, "-");

        sb = new StringBuffer(sb.toString());
        sb.insert(23, "-");

        return sb.toString();
    }
}
