package de.fileinputstream.uuidcache.listeners;

import de.fileinputstream.uuidcache.UUIDCacheBootstrap;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

/**
 * This class has been generated by Alexander on 30.04.18 19:53
 * You are not allowed to edit this resource or other components of it
 * © 2018 Alexander Fiedler
 */
public class ListenerLogin implements Listener {

    @EventHandler
    public void onPlayerLogin(final AsyncPlayerPreLoginEvent event) {
        UUIDCacheBootstrap.getInstance().getUuidCache().getUUID(event.getName(), s -> {

        });
    }

}
