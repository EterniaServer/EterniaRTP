package br.com.eterniaserver.eterniartp.core.baseobjects;

public class WorldHeight {

    private final int min;
    private final int max;

    public WorldHeight(final int min, final int max) {
        this.min = min;
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

}
