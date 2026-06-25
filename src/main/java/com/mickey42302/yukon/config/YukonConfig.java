package com.mickey42302.yukon.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class YukonConfig {
    private static final File CONFIG_FILE = FabricLoader.getInstance()
            .getConfigDir()
            .resolve("yukon.json")
            .toFile();

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static YukonConfig instance;

    public boolean showReconnectButton = true;

    public static YukonConfig getInstance() {
        if (instance == null) {
            load();
        }
        return instance;
    }

    public static void load() {
        if (CONFIG_FILE.exists()) {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                instance = GSON.fromJson(reader, YukonConfig.class);
            } catch (IOException e) {
                System.err.println("Failed to load configuration file.");
                instance = new YukonConfig();
            }
        } else {
            instance = new YukonConfig();
            save();
        }
    }

    public static void save() {
        if (instance == null) instance = new YukonConfig();
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(instance, writer);
        } catch (IOException e) {
            System.err.println("Failed to save configuration file.");
        }
    }
}
