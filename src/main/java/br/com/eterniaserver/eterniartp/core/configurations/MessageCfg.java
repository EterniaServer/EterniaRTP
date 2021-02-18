package br.com.eterniaserver.eterniartp.core.configurations;

import br.com.eterniaserver.eternialib.core.baseobjects.CustomizableMessage;
import br.com.eterniaserver.eternialib.core.enums.ConfigurationCategory;
import br.com.eterniaserver.eternialib.core.interfaces.ReloadableConfiguration;
import br.com.eterniaserver.eterniartp.Constants;
import br.com.eterniaserver.eterniartp.core.enums.Messages;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class MessageCfg implements ReloadableConfiguration {

    private final String[] messages;
    private final CustomizableMessage[] customizableMessages;

    public MessageCfg(final String[] messages) {
        this.messages = messages;
        this.customizableMessages = new CustomizableMessage[Messages.values().length];

        addDefaults(Messages.BANNED_WORLD, "O RTP é proibido no $3{0}$8.", "0: nome do mundo");
        addDefaults(Messages.UNSAFE_WORLD, "O RTP no $3{0} $7não é seguro$8, $7deseja continuar$8?", "0: nome do mundo");

        addDefaults(Messages.ECO_DISABLED, "$3Vault $7não encontrado ou integração desabilitada$8.", null);
        addDefaults(Messages.ECO_PAY_RTP, "Irá custar $3{0} $7para utilizar o random teleport$8, $7para aceitar utilize $6/command accept $7ou $6/command deny$7 para negar$8.", "0: custo do comando");
        addDefaults(Messages.ECO_FREE_RTP, "Se teleportando de graça$8, $7para aceitar utilize $6/command accept $7ou $6/command deny$7 para negar$8.", null);
        addDefaults(Messages.ECO_NO_MONEY, "Você não possui dinheiro suficiênte para isso$8, $7você precisa de $3{0}$8.", "0: custo do comando");

        addDefaults(Messages.MOVED, "Você se moveu o seu teleporte foi cancelado$8.", null);
        addDefaults(Messages.PLAYER_IN_COOLDOWN, "Você ainda não pode utilizar esse comando$8, $7aguarde $3{0} segundos$8.", "0: cooldown");

        addDefaults(Messages.RTP_TELEPORTED, "Espero que tenha achado um local legal :3$8.", null);
        addDefaults(Messages.RTP_TELEPORTING, "Você será teleportado em $3{0} segundos$8.", "0: tempo até ser teleportado");
        addDefaults(Messages.RTP_STARTING, "Você será teleportado em breve$8.", null);
        addDefaults(Messages.RTP_NO_SAFE, "Não foi encontrado um local seguro$8, $7tente novamente$8.", null);
        addDefaults(Messages.RTP_NO_SAFE_REFUND, "Não foi encontrado um local seguro$8, seu dinheiro não foi cobrado$8, $7tente novamente$8.", null);
    }

    private void addDefaults(final Messages entry, final String text, final String note) {
        this.customizableMessages[entry.ordinal()] = new CustomizableMessage(text, note);
    }

    @Override
    public ConfigurationCategory category() {
        return ConfigurationCategory.GENERIC;
    }

    @Override
    public void executeConfig() {

        final FileConfiguration config = YamlConfiguration.loadConfiguration(new File(Constants.MESSAGES_FILE_PATH));

        for (final Messages entry : Messages.values()) {
            CustomizableMessage messageData = customizableMessages[entry.ordinal()];

            if (messageData == null) {
                messageData = new CustomizableMessage("Mensagem faltando para $3" + entry.name() + "$8.", null);
            }

            messages[entry.ordinal()] = config.getString(entry.name() + ".text", messageData.text);
            config.set(entry.name() + ".text", messages[entry.ordinal()]);

            messages[entry.ordinal()] = messages[entry.ordinal()].replace('$', (char) 0x00A7);

            if (messageData.getNotes() != null) {
                messageData.setNotes(config.getString(entry.name() + ".notes", messageData.getNotes()));
                config.set(entry.name() + ".notes", messageData.getNotes());
            }

        }

        new File(Constants.DATA_LOCALE_FOLDER_PATH).mkdir();

        try {
            config.save(Constants.MESSAGES_FILE_PATH);
        } catch (IOException exception) {
            exception.printStackTrace();
        }

    }

    @Override
    public void executeCritical() {

    }


}
