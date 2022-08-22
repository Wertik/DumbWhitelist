package space.devport.wertik;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class WhitelistPlugin extends JavaPlugin {

    private final Set<String> whitelistedPlayers = new HashSet<>();

    private final File file = new File(getDataFolder(), "data.yml");

    private FileConfiguration data;

    private final MiniMessage serializer = MiniMessage.builder().tags(
                    TagResolver.builder()
                            .resolver(StandardTags.color())
                            .resolver(StandardTags.decorations())
                            .resolver(StandardTags.clickEvent())
                            .build())
            .build();

    public MiniMessage getSerializer() {
        return serializer;
    }

    @Override
    public void onEnable() {

        // Load data

        if (!file.exists()) {
            if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
                getLogger().severe("Couldn't create data.yml");
                return;
            }

            try {
                if (!file.createNewFile()) {
                    getLogger().severe("Couldn't create data.yml");
                    return;
                }
            } catch (IOException e) {
                getLogger().severe("Couldn't create data.yml");
                return;
            }
        }

        this.data = YamlConfiguration.loadConfiguration(file);

        whitelistedPlayers.addAll(data.getStringList("whitelist"));
        getLogger().info(String.format("Loaded %d player(s) into the whitelist...", whitelistedPlayers.size()));

        Objects.requireNonNull(getCommand("dumbwhitelist"), "Forgot to add the command into plugin.yml.").setExecutor(new WhitelistCommand(this));
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
    }

    @Override
    public void onDisable() {
        data.set("whitelist", new ArrayList<>(whitelistedPlayers));
        try {
            data.save(file);
        } catch (IOException e) {
            getLogger().severe("Couldn't save data.yml");
        }
    }

    public boolean isWhitelisted(@NotNull String playerName) {
        return this.whitelistedPlayers.contains(playerName);
    }

    public void addPlayer(@NotNull String playerName) {
        this.whitelistedPlayers.add(playerName);
    }

    public void removePlayer(@NotNull String playerName) {
        this.whitelistedPlayers.remove(playerName);
    }

    public Set<String> getWhitelist() {
        return Collections.unmodifiableSet(this.whitelistedPlayers);
    }
}
