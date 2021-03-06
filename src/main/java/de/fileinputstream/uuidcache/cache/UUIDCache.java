package de.fileinputstream.uuidcache.cache;

import de.fileinputstream.uuidcache.UUIDCacheBootstrap;
import de.fileinputstream.uuidcache.cache.backends.MineToolsBackend;
import de.fileinputstream.uuidcache.cache.backends.MojangBackend;
import de.fileinputstream.uuidcache.event.UUIDCachedEvent;
import de.fileinputstream.uuidcache.event.UUIDEvictEvent;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

/**
 * This class has been generated by Alexander on 29.04.18 21:58
 * You are not allowed to edit this resource or other components of it
 * © 2018 Alexander Fiedler
 */

/**
 * This class caches the uuid's.
 * <p>
 * Functions: cacheUUID : Caches the uuid and saves it into the redis database. This entry will expire after the given 'CacheEntryExpire' seconds set in the config of this plugin.
 */
public class UUIDCache {

    public MineToolsBackend mineToolsBackend = new MineToolsBackend();

    public MojangBackend mojangBackend = new MojangBackend();

    public ExecutorService uuidService = Executors.newCachedThreadPool();

    public final HashMap<String, String> uuidCache = new HashMap<>();

    /**
     * Loads the uuid over the redis cache. If no uuid was found to the given name, this method will return "EmptyCacheResult"
     * @param name
     * @param consumer
     */
    public void loadCached(final String name, final Consumer<String> consumer) {
        UUIDCacheBootstrap.getInstance().getRedisService().execute(() -> {
            if(UUIDCacheBootstrap.getInstance().getRedisManager().getJedis().exists("uuidcache:" + name.toLowerCase())) {
                String uuid = UUIDCacheBootstrap.getInstance().getRedisManager().getJedis().hget("uuidcache:" + name.toLowerCase(),"uuid");
                consumer.accept(uuid);
            } else {
                consumer.accept("Empty cache result");
            }
        });
    }

    /**
     * Caches the current uuid into the redis cache.
     * @param name
     * @param uuid
     * @param service
     */
    public void cacheUUID(final String name, final String uuid, final ExecutorService service) {
        service.execute(() -> {
            uuidCache.put(name, uuid);
            UUIDCacheBootstrap.getInstance().getServer().getPluginManager().callEvent(new UUIDCachedEvent(name, uuid));
            UUIDCacheBootstrap.getInstance().getRedisManager().getJedis().hset("uuidcache:" + name.toLowerCase(), "uuid", uuid);
            UUIDCacheBootstrap.getInstance().getRedisManager().getJedis().hset("uuidcache:" + name.toLowerCase(), "cacheHit", String.valueOf(System.currentTimeMillis()));
            UUIDCacheBootstrap.getInstance().getRedisManager().getJedis().expire("uuidcache:" + name.toLowerCase(),UUIDCacheBootstrap.getInstance().getCacheEntryExpire());
        });

    }

    /**
     * Uncached the uuid from the redis cache.
     * @param name
     */
    public void evictUUID(final String name) {
        uuidCache.remove(name);
        UUIDCacheBootstrap.getInstance().getRedisService().execute(() -> {
            getUUID(name, s -> {
                if(UUIDCacheBootstrap.getInstance().getRedisManager().getJedis().exists("uuidcache:" + name.toLowerCase())) {
                    UUIDCacheBootstrap.getInstance().getRedisManager().getJedis().hdel("uuidcache:" + name.toLowerCase(),"uuid",s);
                    UUIDCacheBootstrap.getInstance().getRedisManager().getJedis().hdel("uuidcache:" + name.toLowerCase(),"cacheHit");
                    UUIDCacheBootstrap.getInstance().getRedisManager().getJedis().del("uuidcache:" + name.toLowerCase());
                    System.out.println(  "UUID  " + s + " has been uncached!");
                    UUIDCacheBootstrap.getInstance().getServer().getPluginManager().callEvent(new UUIDEvictEvent(name, s));
                }
            });

        });

    }

    /**
     * Asynchronously returns the UUID of the given player name.
     *
     * @param playerName
     * @param endpointConsumer
     */
    public void getUUID(final String playerName, Consumer<String> endpointConsumer) {
        if (uuidCache.containsKey(playerName)) {
            endpointConsumer.accept(uuidCache.get(playerName));
            return;
        }
        loadCached(playerName, s -> {
            if(s.equalsIgnoreCase("Empty cache result")) {
                for(UUIDBackend backends : UUIDBackend.values()) {
                    if(!backends.isReachable()) continue;
                    if(backends == UUIDBackend.MINETOOLS) {
                        mineToolsBackend.getUUID(playerName, s1 -> {
                            endpointConsumer.accept(s1);
                            cacheUUID(playerName, s1,uuidService);
                            System.out.println("UUID  " + s1 + " has been cached!");
                        });
                    } else if(backends == UUIDBackend.MOJANG) {
                        //Minetools backend may be down, use Mojang backend instead ( 600 requests/10 minutes)
                        mojangBackend.getUUID(playerName, s1 -> {
                            endpointConsumer.accept(s1);
                            cacheUUID(playerName, s1,uuidService);
                            System.out.println("UUID  " + s1 + " has been cached!");
                        });
                    }
                }
            } else {
                endpointConsumer.accept(s);
            }
        });
    }
}
