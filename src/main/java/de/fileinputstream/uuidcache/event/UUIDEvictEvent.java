package de.fileinputstream.uuidcache.event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * This class has been generated by Alexander on 02.05.18 16:40
 * You are not allowed to edit this resource or other components of it
 * © 2018 Alexander Fiedler
 */
public class UUIDEvictEvent extends Event implements Cancellable {

    /**
     * Builds up the {@link UUIDEvictEvent} with the uncachedName and the uncachedUUID
     *
     * @param uncachedName
     * @param uncachedUUID
     */

    private static final HandlerList handlers = new HandlerList();
    public String uncachedName;
    public String uncachedUUID;
    public boolean cancelled = false;

    public UUIDEvictEvent(String uncachedName, String uncachedUUID) {
        this.uncachedName = uncachedName;
        this.uncachedUUID = uncachedUUID;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * Returns the uncached name that was given in the constructor.
     *
     * @return
     */
    public String getUncachedName() {
        return uncachedName;
    }

    /**
     * Returns the uncached uuid that was given in the constructor.
     *
     * @return String
     */
    public String getUncachedUUID() {
        return uncachedUUID;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    /**
     * This method will ever return 'false'. So this method is unnecessary
     */
    @Override
    @Deprecated
    public boolean isCancelled() {
        return false;
    }

    /**
     * It makes no sense to cancel this event. So this method is unnecessary
     *
     * @param b
     */
    @Override
    @Deprecated
    public void setCancelled(boolean b) {
    }
}