package net.minipasglop.bot;

/**
 * Created by Junior on 06/10/2016.
 */
public class MyTimer {
    private static int tempsRestant;

    public MyTimer() {
        this.tempsRestant = 0;
    }

    public static void timing(MyTimer timer) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setTempsRestant(0);
    }

    public static int getTempsRestant() {return tempsRestant;}
    public static void setTempsRestant(int temps){tempsRestant = temps;}
}
