package config;

public abstract class Config {

    public static final boolean DEBUG_ON = true;

    public static final int DECIMAL_ACCURACY = 12;
    public static final double DECIMAL_SCALING = Math.pow(10, Config.DECIMAL_ACCURACY);

}
