package gg.kite.core.events;

import gg.kite.core.entity.FloatingItem;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class FloatingItemRemoveEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private final FloatingItem item;

    public FloatingItemRemoveEvent(FloatingItem item) {
        this.item = item;
    }

    public FloatingItem getItem() { return item; }
    @Override public HandlerList getHandlers() { return handlers; }
    public static HandlerList getHandlerList() { return handlers; }
}