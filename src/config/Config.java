package config;

public abstract class Config {

    public static final boolean DEBUG_ON = false;
    public static final boolean LOAD_FROM_CACHE = false;
    public static final boolean SAVE_TO_CACHE = false;

    public static final int DECIMAL_ACCURACY = 15;
    public static final double DECIMAL_SCALING = Math.pow(10, Config.DECIMAL_ACCURACY);

}
