package br.com.eterniaserver.config;

public class Strings {

    private final Configs configs;

    public Strings(Configs configs) {
        this.configs = configs;
    }

    public String putPrefix(String message) {
        return getMessage("server.prefix") + getMessage(message);
    }

    public String getMessage(String valor) {
        return getColor(getString(valor));
    }

    public String getString(String valor) {
        return configs.messages.getString(valor);
    }

    public String getColor(String valor) {
        return org.bukkit.ChatColor.translateAlternateColorCodes('&', valor);
    }

}
