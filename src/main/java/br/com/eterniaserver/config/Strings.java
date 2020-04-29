package br.com.eterniaserver.config;

public class Strings {

    public static java.lang.String putPrefix(String message) {
        return Strings.getMessage("server.prefix") + Strings.getMessage(message);
    }

    public static String getMessage(String valor) {
        return getColor(getString(valor));
    }

    public static String getString(String valor) {
        return Configs.messages.getString(valor);
    }

    public static String getColor(String valor) {
        return org.bukkit.ChatColor.translateAlternateColorCodes('&', valor);
    }

}
