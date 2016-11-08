package net.minipasglop.bot;

//http://home.dv8tion.net:8080/job/JDA/Promoted%20Build/javadoc/

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


public class Main implements EventListener {

    private JDA jda;

    private final String token = "MjIyNzg4MDUxMjI1NjczNzI4.CrCfFQ.wGH5pTMJVuKBdxTAXyHIlHMsAYc";

    private int nombreCommandes;

    private boolean stop;

    private boolean spam;

    private List<Guild> listeSalonBot;

    private MessageReceivedEventListener mrel;

    private UserUpdateStatusEventListener uusel;

    private AvatarAndNameUpdateListener aanul;

    private static Vector<List<User>> listeUsers;


    Main() {
        try {
            jda = new JDABuilder().setBotToken(token).setBulkDeleteSplittingEnabled(false).buildBlocking();
            jda.addEventListener(this);
            jda.getAccountManager().setGame("www.twitch.tv/minipasglop");
            listeSalonBot = jda.getGuilds();
            listeUsers = new Vector<>(listeSalonBot.size());
            for(int i = 0; i < listeSalonBot.size(); ++i) {
                listeUsers.add(listeSalonBot.get(i).getUsers());
            }
            Tools.setListeUsers(listeUsers);
            Tools.setListeSalonsBot(listeSalonBot);
            Tools.setJda(jda);
            new FileWriter(new File("LogCo.txt")).close();
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
        mrel = new MessageReceivedEventListener(this);
        uusel = new UserUpdateStatusEventListener();
        aanul = new AvatarAndNameUpdateListener();
        stop = false;
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
            }
            if (cmd.equalsIgnoreCase("tg")) {
                System.out.println("Arret du spam... :'( ");
                spam = false;
            }
        }
    }//Constructeur de la JDA permettant de faire fonctionner le bot et le couper en tapant stop dans la console

    public static void main(String[] args) {
        new Main();
    }//Fonction main

    public boolean isSpam() {
        return spam;
    }

    public void setSpam(boolean spam) {
        this.spam = spam;
    }

    public List<Guild> getListeSalonBot() {
        return listeSalonBot;
    }

    public JDA getJda() {
        return jda;
    }

    @Override
    public void onEvent(Event event) {
        if (event instanceof MessageReceivedEvent) {
            MessageReceivedEvent e = (MessageReceivedEvent) event;
            mrel.use(e);
            nombreCommandes++;
        }

        if (event instanceof UserOnlineStatusUpdateEvent) {
            UserOnlineStatusUpdateEvent ev = (UserOnlineStatusUpdateEvent) event;
            uusel.use(ev);
            nombreCommandes++;
        }// Partie relative au listener gerant le bonjour personnalise A LA CONNECTION PUTAIN C'EST COOL CA BOBBY

        if (event instanceof UserAvatarUpdateEvent) {
            aanul.useAva((UserAvatarUpdateEvent) event);
            nombreCommandes++;

            if (event instanceof UserNameUpdateEvent) {
                aanul.useName(event);
                nombreCommandes++;
            }
        }
    }
}