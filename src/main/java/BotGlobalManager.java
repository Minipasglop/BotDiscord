import listeners.MessageListener;
import listeners.UserMovementListener;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;

import javax.security.auth.login.LoginException;
import java.util.Scanner;

public class BotGlobalManager {
    private static JDA jda;
    private final String token = "MjIyNzg4MDUxMjI1NjczNzI4.Db-5ew.CLnuT4PWR007A_k-mcRvOWKZqnw";


    BotGlobalManager() {
        try {
            jda = new JDABuilder(AccountType.BOT).setGame(Game.of(Game.GameType.WATCHING,"Work In Progress")).setToken(token).setBulkDeleteSplittingEnabled(false).buildBlocking();
            jda.addEventListener(new MessageListener());
            jda.addEventListener(new UserMovementListener());
        } catch (LoginException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("Une erreur est survenue veuillez verifier le token ou votre connection internet");
            return;
        }
        boolean stop = false;
        while (!stop) {
            Scanner scanner = new Scanner(System.in);
            String cmd = scanner.next();
            if (cmd.equalsIgnoreCase("stop")) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                jda.shutdown();
                stop = true;
            }//Arreter le bot proprement en tapant "stop" dans la console, il nous affichera alors le nombre de commande qu'il a effectué et attendra 5 secondes avant de mourir... Pauvre Jackson :'(
        }
    }//Constructeur de la JDA permettant de faire fonctionner le bot et le couper en tapant stop dans la console

    public static void main(String[] args) {
        new BotGlobalManager();
    }//Fonction main
}
