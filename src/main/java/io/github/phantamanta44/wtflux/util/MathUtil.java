package io.github.phantamanta44.wtflux.util;

public final class MathUtil {

    public static final float FLUX_CONST = 0.02F;
    public static final float HEAT_CONST = 23.0F;
    public static final float COR_CONST = 0.00386F;
    public static final double TIME_CONV_CONST = 14350.0;

    public static float voltageFromFlux(float momentum, int coils) {
        return Math.max((float)coils * momentum * FLUX_CONST, 0);
    }

    public static float resistanceFromHeat(float temp) {
        return Math.max(1 + COR_CONST * (temp - HEAT_CONST), 0.001F);
    }

    public static double timeToRad(int time) {
        return ((double)(time + 1350) / TIME_CONV_CONST) * Math.PI;
    }

}
