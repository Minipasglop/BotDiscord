package net.minipasglop.bot;


//Classe asser simple permettant de faire fonctionner la Méthode canDoCommand() dans la classe MessageReceivedEventListener
//En soi, permets d'éviter les spammeurs que tout le monde déteste, et que Jackson exècre.

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
    }//timing

    public static int getTempsRestant() {return tempsRestant;}
    public static void setTempsRestant(int temps){tempsRestant = temps;}
}
