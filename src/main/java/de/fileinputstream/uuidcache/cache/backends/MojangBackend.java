package de.fileinputstream.uuidcache.cache.backends;

import org.bukkit.Bukkit;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.logging.Level;

/**
 * © Alexander Fiedler 2018 - 2019
 * <p>
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * You may not use parts of this program as long as you were not given permission.
 * You may not distribute this project or parts of it under you own name, you company name or someone other's name.
 * Created: 14.12.2018 at 19:49
 */
public class MojangBackend {

    public ExecutorService service = Executors.newCachedThreadPool();


    public void getUUID(String username, Consumer<String> consumer) {


        service.execute(() -> {
            try {
                URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + username);
                Bukkit.getLogger().log(Level.WARNING,"Using Mojang UUID method because MineTools Backend is down. \n" +
                        "UUIDCache may fail within the next 30 minutes because of rate limiting from Mojang!");
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
                consumer.accept("An unknown error occurred,");
            }
        });
    }
    public String insertDashUUID(String uuid) {
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
