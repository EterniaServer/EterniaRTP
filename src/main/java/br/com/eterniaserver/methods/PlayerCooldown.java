package br.com.eterniaserver.methods;

import br.com.eterniaserver.config.Configs;

public class PlayerCooldown {

    private int cooldown;

    public PlayerCooldown(Configs configs) {
        this.cooldown = configs.configs.getInt("server.cooldown");
    }

    public int getCountdown() {
        return cooldown;
    }

    public void decreaseCountdown() {
        cooldown -= 1;
    }

}
