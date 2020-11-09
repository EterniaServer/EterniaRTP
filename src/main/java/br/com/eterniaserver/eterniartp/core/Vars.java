package br.com.eterniaserver.eterniartp.core;

import java.util.HashMap;
import java.util.UUID;

public class Vars {

    private Vars() {
        throw new IllegalStateException("Utility class");
    }

    protected static final HashMap<UUID, Long> rtp = new HashMap<>();

}
