package br.com.eterniaserver.eterniartp.configurations.locales;

import br.com.eterniaserver.eterniartp.Constants;
import br.com.eterniaserver.eterniartp.EterniaRTP;
import br.com.eterniaserver.eterniartp.enums.ConfigStrings;
import br.com.eterniaserver.eterniartp.enums.Messages;
import br.com.eterniaserver.eterniartp.objetos.CustomizableMessage;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MsgCfg {

    public MsgCfg(String[] messages) {

        Map<String, CustomizableMessage> defaults = new HashMap<>();

        this.addDefault(defaults, Messages.RTP_RELOAD, "Reiniciando configurações e mensagens$8.", null);
        this.addDefault(defaults, Messages.BANNED_WORLD, "É proibido utilizar o RTP nesse mundo$8.", null);
        this.addDefault(defaults, Messages.RTP_TELEPORT, "Aguarde você será teleportado em breve$8.", null);
        this.addDefault(defaults, Messages.TELEPORTED, "Você foi teleportado e isso lhe custou $3{0}$8.", "0: preço");
        this.addDefault(defaults, Messages.TELEPORTED_FREE, "Você foi teleportado de graça$8.", null);
        this.addDefault(defaults, Messages.NOT_SAFE, "Nenhum local seguro encontrado$8, $7RTP cancelado$8.", null);
        this.addDefault(defaults, Messages.REFUND, "Você recebeu um reembolso de $3{0}$8.", "0: preço");
        this.addDefault(defaults, Messages.NO_MONEY, "Você não possui dinheiro suficiênte$8, $7você precisa de $3{0}$8.", "0: preço");
        this.addDefault(defaults, Messages.WAIT, "Aguarde $3{0} $7segundos$8,$7 para poder se teleportar$8.", "0: tempo");

        FileConfiguration config = YamlConfiguration.loadConfiguration(new File(Constants.MESSAGES_FILE_PATH));

        for (Messages messagesEnum : Messages.values()) {
            CustomizableMessage messageData = defaults.get(messagesEnum.name());

            if (messageData == null) {
                messageData = new CustomizableMessage(messagesEnum, EterniaRTP.getString(ConfigStrings.SERVER_PREFIX) +"Mensagem faltando para $3" + messagesEnum.name() + "$8.", null);
            }

            messages[messagesEnum.ordinal()] = config.getString(messagesEnum.name() + ".text", messageData.text);
            config.set(messagesEnum.name() + ".text", messages[messagesEnum.ordinal()]);

            messages[messagesEnum.ordinal()] = messages[messagesEnum.ordinal()].replace('$', (char) 0x00A7);

            if (messageData.getNotes() != null) {
                messageData.setNotes(config.getString(messagesEnum.name() + ".notes", messageData.getNotes()));
                config.set(messagesEnum.name() + ".notes", messageData.getNotes());
            }

        }

        new File(Constants.DATA_LOCALE_FOLDER_PATH).mkdir();

        try {
            config.save(Constants.MESSAGES_FILE_PATH);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private void addDefault(Map<String, CustomizableMessage> defaults, Messages id, String text, String notes) {
        CustomizableMessage message = new CustomizableMessage(id, text, notes);
        defaults.put(id.name(), message);
    }

}