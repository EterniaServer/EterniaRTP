package br.com.eterniaserver.eterniartp.core.configurations;

import br.com.eterniaserver.eternialib.chat.MessageMap;
import br.com.eterniaserver.eternialib.configuration.enums.ConfigurationCategory;
import br.com.eterniaserver.eternialib.configuration.interfaces.MsgConfiguration;
import br.com.eterniaserver.eterniartp.Constants;
import br.com.eterniaserver.eterniartp.core.enums.Messages;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class MessageCfg implements MsgConfiguration<Messages> {

    private final MessageMap<Messages, String> messages = new MessageMap<>(Messages.class, Messages.RTP_PREFIX);

    private final FileConfiguration inFile;
    private final FileConfiguration outFile;

    public MessageCfg() {
        this.inFile = YamlConfiguration.loadConfiguration(new File(getFilePath()));
        this.outFile = new YamlConfiguration();
    }


    @Override
    public FileConfiguration inFileConfiguration() {
        return inFile;
    }

    @Override
    public FileConfiguration outFileConfiguration() {
        return outFile;
    }

    @Override
    public MessageMap<Messages, String> messages() {
        return messages;
    }

    @Override
    public ConfigurationCategory category() {
        return ConfigurationCategory.GENERIC;
    }

    @Override
    public String getFolderPath() {
        return Constants.DATA_LOCALE_FOLDER_PATH;
    }

    @Override
    public String getFilePath() {
        return Constants.MESSAGES_FILE_PATH;
    }

    @Override
    public void executeConfig() {
        addMessage(Messages.RTP_PREFIX, "#555555[#34eb40E#3471ebR#555555]#AAAAAA ");
        addMessage(Messages.BANNED_WORLD, "O RTP é proibido no #{0}#555555.", "0: nome do mundo");
        addMessage(Messages.UNSAFE_WORLD, "O RTP no #00aaaa{0} #aaaaaanão é seguro#555555, #aaaaaadeseja continuar#555555?", "0: nome do mundo");

        addMessage(Messages.ECO_DISABLED, "#00aaaaVault #aaaaaanão encontrado ou integração desabilitada#555555.");
        addMessage(Messages.ECO_PAY_RTP, "Irá custar #00aaaa{0} #aaaaaapara utilizar o random teleport#555555.", "0: custo do comando");
        addMessage(Messages.ECO_FREE_RTP, "Se teleportando de graça#555555, #aaaaaapara aceitar utilize #ffaa00/command accept #aaaaaaou #ffaa00/command deny#aaaaaa para negar#555555.");
        addMessage(Messages.ECO_NO_MONEY, "Você não possui dinheiro suficiênte para isso#555555, #aaaaaavocê precisa de #00aaaa{0}#555555.", "0: custo do comando");

        addMessage(Messages.MOVED, "Você se moveu o seu teleporte foi cancelado#555555.");
        addMessage(Messages.PLAYER_IN_COOLDOWN, "Você ainda não pode utilizar esse comando#555555, #aaaaaaaguarde #00aaaa{0} segundos#555555.", "0: cooldown");

        addMessage(Messages.RTP_TELEPORTING, "Você será teleportado em breve#555555.");
        addMessage(Messages.RTP_STARTING, "Você será teleportado em breve#555555.");
        addMessage(Messages.RTP_NO_SAFE, "Não foi encontrado um local seguro#555555, #aaaaaatente novamente#555555.");
        addMessage(Messages.RTP_NO_SAFE_REFUND, "Não foi encontrado um local seguro#555555, #aaaaaaseu dinheiro não foi cobrado#555555, #aaaaaatente novamente#555555.");
        addMessage(Messages.RTP_ALREADY_IN_TIMING, "Você já está em processo de teleport#555555.");
        addMessage(Messages.RTP_ALREADY_IN_CONFIRMATION,
                "Você já está em um processo de confirmação#555555."
        );
    }

    @Override
    public void executeCritical() { }
}
