package br.com.eterniaserver.eterniartp.core.configurations;

import br.com.eterniaserver.eternialib.configuration.CommandLocale;
import br.com.eterniaserver.eternialib.configuration.ReloadableConfiguration;
import br.com.eterniaserver.eternialib.configuration.enums.ConfigurationCategory;
import br.com.eterniaserver.eterniartp.Constants;
import br.com.eterniaserver.eterniartp.EterniaRTP;
import br.com.eterniaserver.eterniartp.core.enums.Messages;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class MessageCfg implements ReloadableConfiguration {

    private final String[] messages;

    private final FileConfiguration inFile;
    private final FileConfiguration outFile;

    public MessageCfg(EterniaRTP plugin) {
        this.messages = plugin.messages();

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
    public String[] messages() {
        return messages;
    }

    @Override
    public CommandLocale[] commandsLocale() {
        return new CommandLocale[0];
    }

    @Override
    public ConfigurationCategory category() {
        return ConfigurationCategory.GENERIC;
    }

    @Override
    public String getFolderPath() {
        return Constants.DATA_LAYER_FOLDER_PATH;
    }

    @Override
    public String getFilePath() {
        return Constants.CONFIG_FILE_PATH;
    }

    @Override
    public void executeConfig() {
        addMessage(Messages.BANNED_WORLD, "O RTP é proibido no <color:#>{0}<color:#555555>.", "0: nome do mundo");
        addMessage(Messages.UNSAFE_WORLD, "O RTP no <color:#00aaaa>{0} <color:#aaaaaa>não é seguro<color:#555555>, <color:#aaaaaa>deseja continuar<color:#555555>?", "0: nome do mundo");

        addMessage(Messages.ECO_DISABLED, "<color:#00aaaa>Vault <color:#aaaaaa>não encontrado ou integração desabilitada<color:#555555>.");
        addMessage(Messages.ECO_PAY_RTP, "Irá custar <color:#00aaaa>{0} <color:#aaaaaa>para utilizar o random teleport<color:#555555>.", "0: custo do comando");
        addMessage(Messages.ECO_FREE_RTP, "Se teleportando de graça<color:#555555>, <color:#aaaaaa>para aceitar utilize <color:#ffaa00>/command accept <color:#aaaaaa>ou <color:#ffaa00>/command deny<color:#aaaaaa> para negar<color:#555555>.");
        addMessage(Messages.ECO_NO_MONEY, "Você não possui dinheiro suficiênte para isso<color:#555555>, <color:#aaaaaa>você precisa de <color:#00aaaa>{0}<color:#555555>.", "0: custo do comando");

        addMessage(Messages.MOVED, "Você se moveu o seu teleporte foi cancelado<color:#555555>.");
        addMessage(Messages.PLAYER_IN_COOLDOWN, "Você ainda não pode utilizar esse comando<color:#555555>, <color:#aaaaaa>aguarde <color:#00aaaa>{0} segundos<color:#555555>.", "0: cooldown");

        addMessage(Messages.RTP_TELEPORTING, "Você será teleportado em breve<color:#555555>.");
        addMessage(Messages.RTP_STARTING, "Você será teleportado em breve<color:#555555>.");
        addMessage(Messages.RTP_NO_SAFE, "Não foi encontrado um local seguro<color:#555555>, <color:#aaaaaa>tente novamente<color:#555555>.");
        addMessage(Messages.RTP_NO_SAFE_REFUND, "Não foi encontrado um local seguro<color:#555555>, <color:#aaaaaa>seu dinheiro não foi cobrado<color:#555555>, <color:#aaaaaa>tente novamente<color:#555555>.");
        addMessage(Messages.RTP_ALREADY_IN_TIMING, "Você já está em processo de teleport<color:#555555>.");
        addMessage(Messages.RTP_ALREADY_IN_CONFIRMATION,
                "Você já está em um processo de confirmação<color:#555555>."
        );
    }

    @Override
    public void executeCritical() { }
}
