package utils;

import java.awt.*;
import java.util.Random;

public class RandomColorGenerator {

    public RandomColorGenerator(){
    }

    public static Color generateRandomColor(){
        Random baseRandom = new Random();
        float r = baseRandom.nextFloat();
        float g = baseRandom.nextFloat();
        float b = baseRandom.nextFloat();
        return new Color(r, g, b);
    }
}
