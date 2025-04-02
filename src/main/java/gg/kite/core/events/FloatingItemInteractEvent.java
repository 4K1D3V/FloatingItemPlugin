package gg.kite.core.events;

import gg.kite.core.entity.FloatingItem;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class FloatingItemInteractEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final FloatingItem item;
    private boolean cancelled = false;

    public FloatingItemInteractEvent(Player player, FloatingItem item) {
        this.player = player;
        this.item = item;
    }

    public Player getPlayer() { return player; }
    public FloatingItem getItem() { return item; }
    @Override public HandlerList getHandlers() { return handlers; }
    public static HandlerList getHandlerList() { return handlers; }
    @Override public boolean isCancelled() { return cancelled; }
    @Override public void setCancelled(boolean cancelled) { this.cancelled = cancelled; }
}