package br.com.eterniaserver.eterniartp;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

public class Strings {

    private Strings() {
        throw new IllegalStateException("Utility class");
    }

    public static void reloadMessages(FileConfiguration msg) {
        M_SERVER_PREFIX = getColor(msg.getString("server.prefix"));
        MSG_WORLD_BANNED = putPrefix(msg, "rtp.worldb");
        MSG_TELEP = putPrefix(msg, "rtp.telep");
        MSG_NO_SAFE = putPrefix(msg, "rtp.no-safe");
        MSG_ECON_GIVE = putPrefix(msg, "econ.give");
        MSG_SUC_MONEY = putPrefix(msg, "rtp.suc-money");
        MSG_SUC_FREE = putPrefix(msg, "rtp.suc-free");
        MSG_NO_MONEY = putPrefix(msg, "econ.no-money");
        MSG_RTP_WAIT = putPrefix(msg, "rtp.wait");
        MSG_RTP_RELOAD = putPrefix(msg, "server.reload");
        MSG_RTP_MODULE = putPrefix(msg, "server.load-data");
    }

    public static final String TIME = "time";
    public static final String UUID = "uuid";

    public static String M_SERVER_PREFIX;
    public static String MSG_WORLD_BANNED;
    public static String MSG_TELEP;
    public static String MSG_NO_SAFE;
    public static String MSG_ECON_GIVE;
    public static String MSG_SUC_MONEY;
    public static String MSG_SUC_FREE;
    public static String MSG_NO_MONEY;
    public static String MSG_RTP_WAIT;
    public static String MSG_RTP_RELOAD;
    public static String MSG_RTP_MODULE;

    private static String putPrefix(FileConfiguration msg, String path) {
        String message = msg.getString(path);
        if (message == null) message = "&7Erro&8, &7texto &3" + path + "&7não encontrado&8.";
        return M_SERVER_PREFIX + getColor(message);
    }

    public static String getColor(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

}
