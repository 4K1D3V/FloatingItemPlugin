package gg.kite.core.entity;

import gg.kite.core.Main;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public final class FloatingItem {
    private final UUID id = UUID.randomUUID();
    private final ArmorStand armorStand;
    private Location baseLocation;
    private final Main plugin;
    private final ItemStack item;
    private final int customModelData;
    private double rotation = 0.0;
    private double timeAccumulator = 0.0;
    private AnimationPattern pattern;

    public FloatingItem(Main plugin, Location location, ItemStack item, int customModelData) {
        this.plugin = Objects.requireNonNull(plugin);
        this.baseLocation = location.clone().add(0, plugin.configManager().getSpawnHeight(), 0);
        this.pattern = plugin.configManager().getDefaultPattern()
            .map(AnimationPattern::fromString)
            .orElse(new AnimationPattern.Sine());
        this.item = item.clone();
        this.customModelData = Math.clamp(customModelData, 0, Integer.MAX_VALUE);
        this.armorStand = spawnArmorStand(modifyItem(item));
    }

    private ItemStack modifyItem(ItemStack item) {
        Optional.ofNullable(item.getItemMeta())
            .ifPresent(meta -> {
                meta.setCustomModelData(customModelData);
                item.setItemMeta(meta);
            });
        return item;
    }

    private ArmorStand spawnArmorStand(ItemStack item) {
        return baseLocation.getWorld().spawn(baseLocation, ArmorStand.class, stand -> {
            stand.setInvisible(true);
            stand.setInvulnerable(true);
            stand.setGravity(false);
            stand.setSmall(true);
            stand.getEquipment().setHelmet(item);
        });
    }

    public void updatePosition(double deltaTime) {
        timeAccumulator += deltaTime;
        double amplitude = plugin.configManager().getHoverAmplitude();
        double hoverOffset = pattern.apply(timeAccumulator) * amplitude;
        rotation += plugin.configManager().getRotationSpeed() * deltaTime;

        Location newLoc = baseLocation.clone().add(0, hoverOffset, 0);
        armorStand.teleport(newLoc);
        armorStand.setHeadPose(new EulerAngle(0, rotation, 0));

        if (plugin.configManager().useParticles()) {
            plugin.configManager().getParticleType()
                .map(Particle::valueOf)
                .ifPresent(particle -> 
                    armorStand.getWorld().spawnParticle(
                        particle,
                        newLoc,
                        plugin.configManager().getParticleCount()
                    ));
        }
    }

    public void remove() {
        Optional.ofNullable(armorStand)
            .filter(a -> !a.isDead())
            .ifPresent(ArmorStand::remove);
    }

    public void move(Location newLocation) {
        this.baseLocation = newLocation.clone().add(0, plugin.configManager().getSpawnHeight(), 0);
    }

    public UUID id() { return id; }
    public void setPattern(AnimationPattern pattern) { this.pattern = Objects.requireNonNull(pattern); }
    public Location getLocation() { return armorStand.getLocation(); }
    public ItemStack getItem() { return item.clone(); }
    public int getCustomModelData() { return customModelData; }
    public ArmorStand getArmorStand() { return armorStand; }
}