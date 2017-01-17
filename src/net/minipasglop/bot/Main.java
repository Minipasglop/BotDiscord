package net.minipasglop.bot;

//http://home.dv8tion.net:8080/job/JDA/Promoted%20Build/javadoc/  <--- Lien vers la Doc de l'API utilisée, très utile, bien fournie... Je recommande :D

import net.dv8tion.jda.JDA;
import net.dv8tion.jda.JDABuilder;
import net.dv8tion.jda.entities.*;
import net.dv8tion.jda.events.Event;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.events.user.UserAvatarUpdateEvent;
import net.dv8tion.jda.events.user.UserNameUpdateEvent;
import net.dv8tion.jda.events.user.UserOnlineStatusUpdateEvent;
import net.dv8tion.jda.hooks.EventListener;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

//La classe Main du projet, celle qui crée le bot, et gère les différentes classes listener au bon moment en fonction des events afin de faire des choses O_o
public class Main implements EventListener {

    private static JDA jda;
    private final String token = "MjIyNzg4MDUxMjI1NjczNzI4.CrCfFQ.wGH5pTMJVuKBdxTAXyHIlHMsAYc";
    private int nombreCommandes;
    private static boolean spam;
    private MessageReceivedEventListener mrel;
    private UserUpdateStatusEventListener uusel;
    private AvatarAndNameUpdateListener aanul;
    private static Vector<List<User>> listeUsers;


    Main() {
        try {
            jda = new JDABuilder().setBotToken(token).setBulkDeleteSplittingEnabled(false).buildBlocking();
            jda.addEventListener(this);
            jda.getAccountManager().setGame("www.twitch.tv/minipasglop");
            listeUsers = new Vector<>(jda.getGuilds().size());
            for(int i = 0; i < jda.getGuilds().size(); ++i)
                listeUsers.add(jda.getGuilds().get(i).getUsers());
            new FileWriter(new File("LogCo.txt")).close();
            mrel = new MessageReceivedEventListener();
        } catch (LoginException | InterruptedException e) {
            e.printStackTrace();
            System.out.println("Une erreur est survenue veuillez verifier le token ou votre connection internet");
            return;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Connecte avec: " + jda.getSelfInfo().getUsername());
        int i;
        System.out.println("Le bot est autorisé sur " + (i = jda.getGuilds().size()) + " serveur" + (i > 1 ? "s" : ""));
        uusel = new UserUpdateStatusEventListener();
        aanul = new AvatarAndNameUpdateListener();
        boolean stop = false;
        spam = false;
        nombreCommandes = 0;
        while (!stop) {
            Scanner scanner = new Scanner(System.in);
            String cmd = scanner.next();
            if (cmd.equalsIgnoreCase("stop")) {
                System.out.println("Le bot a fait " + nombreCommandes + " commandes.");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                jda.shutdown(true);
                stop = true;
            }//Arreter le bot proprement en tapant "stop" dans la console, il nous affichera alors le nombre de commande qu'il a effectué et attendra 5 secondes avant de mourir... Pauvre Jackson :'(
            if (cmd.equalsIgnoreCase("tg")) {
                System.out.println("Arret du spam... :'( ");
                spam = false;
            }//Permets d'arreter de spammer.= quand la commande de spam est lancée par mes soins.
        }
    }//Constructeur de la JDA permettant de faire fonctionner le bot et le couper en tapant stop dans la console

    public static void main(String[] args) {
        new Main();
    }//Fonction main

    public static boolean isSpam() {
        return spam;
    }

    public static void setSpam(boolean spam) {
        spam = spam;
    }

    public static List<Guild> getListeSalonBot() {
        return jda.getGuilds();
    }

    public static JDA getJda() {
        return jda;
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof MessageReceivedEvent) {
            MessageReceivedEvent e = (MessageReceivedEvent) event;
            mrel.use(e);
            nombreCommandes++;
        }//Partie relative au listener gérant les commandes

        if (event instanceof UserOnlineStatusUpdateEvent) {
            UserOnlineStatusUpdateEvent ev = (UserOnlineStatusUpdateEvent) event;
            uusel.use(ev);
            nombreCommandes++;
        }// Partie relative au listener gerant le bonjour personnalise

        if (event instanceof UserAvatarUpdateEvent) {
            aanul.useAva((UserAvatarUpdateEvent) event);
            nombreCommandes++;
        }//Partie relative au listener gérant les modifications d'avatar

        if (event instanceof UserNameUpdateEvent) {
            aanul.useName(event);
            nombreCommandes++;
        }//Partie relative au listener gérant les modifications de nom d'utilisateur

    }//onEvent(), pas la meilleure facon de le faire avec les instanceof :/ Mais comme on dit Inshallah ca marche ^^
}