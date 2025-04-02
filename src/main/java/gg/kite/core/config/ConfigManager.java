package gg.kite.core.config;

import gg.kite.core.Main;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public record ConfigManager(Main plugin) {
    public void reload() {
        plugin.reloadConfig();
        validateConfig();
    }

    private FileConfiguration config() { return plugin.getConfig(); }

    public double getHoverAmplitude() {
        return Math.clamp(config().getDouble("animation.hover-amplitude", 0.1), 0.0, 1.0);
    }

    public double getRotationSpeed() {
        return Math.clamp(config().getDouble("animation.rotation-speed", 0.05), 0.0, 1.0);
    }

    public long getUpdateFrequency() {
        return Math.clamp(config().getLong("animation.update-frequency", 1L), 1L, 20L);
    }

    public Optional<String> getDefaultPattern() {
        String value = config().getString("animation.default-pattern", "SINE");
        return Optional.ofNullable(value).filter(p -> 
            p.equalsIgnoreCase("SINE") || p.equalsIgnoreCase("BOUNCE") || p.equalsIgnoreCase("LINEAR"));
    }

    public double getSpawnHeight() {
        return Math.clamp(config().getDouble("item.spawn-height", 1.5), 0.5, 3.0);
    }

    public boolean isItemTypeAllowed(Material type) {
        List<String> allowed = config().getStringList("item.allowed-types");
        return allowed.isEmpty() || allowed.contains(type.name());
    }

    public boolean useParticles() { return config().getBoolean("effects.particles", false); }

    public Optional<String> getParticleType() {
        String value = config().getString("effects.particle-type", "FLAME");
        return Optional.ofNullable(value).filter(p -> {
            try {
                Particle.valueOf(p.toUpperCase());
                return true;
            } catch (IllegalArgumentException e) {
                return false;
            }
        });
    }

    public int getParticleCount() {
        return (int) Math.clamp(config().getInt("effects.particle-count", 2), 1, 50);
    }

    public Optional<String> getInteractParticle() {
        String value = config().getString("effects.interact-particle", "HEART");
        return Optional.ofNullable(value).filter(p -> {
            try {
                Particle.valueOf(p.toUpperCase());
                return true;
            } catch (IllegalArgumentException e) {
                return false;
            }
        });
    }

    public int getInteractParticleCount() {
        return (int) Math.clamp(config().getInt("effects.interact-particle-count", 5), 1, 50);
    }

    public Sound getSuccessSound() {
        return Sound.valueOf(config().getString("sounds.success", "ENTITY_EXPERIENCE_ORB_PICKUP").toUpperCase());
    }

    public Sound getErrorSound() {
        return Sound.valueOf(config().getString("sounds.error", "BLOCK_ANVIL_LAND").toUpperCase());
    }

    public Sound getInfoSound() {
        return Sound.valueOf(config().getString("sounds.info", "BLOCK_NOTE_BLOCK_PLING").toUpperCase());
    }

    public Sound getWarningSound() {
        return Sound.valueOf(config().getString("sounds.warning", "ENTITY_VILLAGER_NO").toUpperCase());
    }

    public Sound getInteractSound() {
        return Sound.valueOf(config().getString("sounds.interact", "ENTITY_ITEM_PICKUP").toUpperCase());
    }

    public float getSoundVolume() {
        return (float) Math.clamp(config().getDouble("sounds.volume", 1.0), 0.0, 2.0);
    }

    public float getSoundPitch() {
        return (float) Math.clamp(config().getDouble("sounds.pitch", 1.0), 0.5, 2.0);
    }

    public String getInteractAction() {
        String action = config().getString("interact.action", "PICKUP").toUpperCase();
        return Set.of("NONE", "PICKUP", "REMOVE", "TELEPORT", "COMMAND").contains(action) ? action : "PICKUP";
    }

    public String getInteractCommand() {
        return config().getString("interact.command", "say %player% interacted with a floating item");
    }

    public boolean isWorldAllowed(String worldName) {
        List<String> allowed = config().getStringList("worlds.allowed-worlds");
        return allowed.isEmpty() || allowed.contains(worldName);
    }

    public boolean isBackupEnabled() { return config().getBoolean("backup.enabled", true); }

    public int getMaxBackups() {
        return (int) Math.clamp(config().getInt("backup.max-backups", 5), 1, 10);
    }

    private void validateConfig() {
        getDefaultPattern().orElseGet(() -> {
            plugin.getLogger().warning("Invalid default-pattern in config, using SINE");
            return "SINE";
        });
        getParticleType().orElseGet(() -> {
            plugin.getLogger().warning("Invalid particle-type in config, using FLAME");
            return "FLAME";
        });
        getInteractParticle().orElseGet(() -> {
            plugin.getLogger().warning("Invalid interact-particle in config, using HEART");
            return "HEART";
        });
        try {
            getSuccessSound();
            getErrorSound();
            getInfoSound();
            getWarningSound();
            getInteractSound();
        } catch (IllegalArgumentException e) {
            plugin.getLogger().warning("Invalid sound name in config, using defaults: " + e.getMessage());
        }
    }
}