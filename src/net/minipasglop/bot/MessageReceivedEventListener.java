package net.minipasglop.bot;

import net.dv8tion.jda.MessageHistory;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.entities.VoiceChannel;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.managers.AudioManager;

import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.File;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import org.apache.commons.lang3.StringUtils;



public class MessageReceivedEventListener {

    private MyTimer monTimer;
    private Main instance;
    private AudioManager audioManage1;
    private AudioManager audioManage2;
    private boolean activeAudio1;
    private boolean activeAudio2;
    private List<VoiceChannel> listeSalonsAudio;
    private MyUrlPlayer djJackson;
    private static boolean djJacksonOn;

    public MessageReceivedEventListener(Main jda) {
        instance = jda;
        monTimer = new MyTimer();
        listeSalonsAudio = instance.getJda().getVoiceChannels();
        audioManage1 = instance.getJda().getAudioManager(Tools.getListeSalonsBot().get(0));
        audioManage2 = instance.getJda().getAudioManager(Tools.getListeSalonsBot().get(1));
        activeAudio1 = false;
        activeAudio2 = false;
        try {
            djJackson = new MyUrlPlayer(instance.getJda());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        djJacksonOn = false;
    }

    private void fonctionSpam(MessageReceivedEvent e) {
        int cpt = 0;
        instance.setSpam(true);
        long timer;
        while (cpt < 10000) {
            if (!instance.isSpam())
                return;
            e.getChannel().sendTyping();
            e.getChannel().sendMessage("Je suis jackson le gentil poulet. Rejoins la jackson family ! : http://steamcommunity.com/groups/Jacksonity ");
            cpt++;
            if (cpt % 5 == 0)
                timer = 3000;
            else
                timer = 1000;
            try {
                Thread.sleep(timer);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }
    } // Spam jusqu'a ce que je tape tg dans la console.

    private static void clearChat(MessageReceivedEvent e) {
        MessageHistory historique = e.getTextChannel().getHistory();
        e.getTextChannel().deleteMessages(historique.retrieveAll());
        e.getTextChannel().sendTyping();
        e.getChannel().sendMessage("La salle de chat a été nettoyée. :see_no_evil: ");
    }

    private static void clearXMessages(MessageReceivedEvent e) {
        MessageHistory historique = e.getTextChannel().getHistory();
        String message = e.getMessage().getContent();
        int nombre = 0;
        String entier = message.substring(9);
        nombre = Integer.parseInt(entier);
        nombre++;
        e.getTextChannel().deleteMessages(historique.retrieve(nombre));
        e.getTextChannel().sendTyping();
        e.getChannel().sendMessage((nombre - 1) + " messages ont été supprimés. ( +cleanup " + (nombre - 1) + " )");
    }// On efface X messages

    private void displayList(TextChannel Salon) {
        Salon.sendTyping();
        String[] TabCommandes = new String[]{"+s", "+twitch mini", "+site b4", "+clear" , "+cat", "+cleanup XX ", "CList ","+tg"};
        String Message = "```";
        for (int i = 0; i < TabCommandes.length - 1; ++i) {
            Message += "\n";
            String Ligne = TabCommandes[i];
            for (int j = TabCommandes[i].length(); j < 35; ++j)
                Ligne += " ";
            Ligne += TabCommandes[++i];
            Message += Ligne;
        }
        Message += "```";
        Salon.sendMessage(Message);
    }//Affiche la liste des commandes. Penser à mettre à jour à l'ajout de new commandes.

    private boolean canDoCommand(MessageReceivedEvent e) {
        if (monTimer.getTempsRestant() != 0) {
            e.getChannel().sendTyping();
            e.getChannel().sendMessage(Tools.getMentionFromUser(e.getAuthor()) + " ferme ta gueule.");
            return false;
        } else {
            monTimer.setTempsRestant(5);
            new SwingWorker<Void, Void>() {
                public Void doInBackground() {
                    MyTimer.timing(monTimer);
                    return null;
                }
            }.execute();
            return true;
        }

    }

    private String fonctionCat() throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL("http://random.cat/meow").openConnection();
        conn.connect();

        BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());

        byte[] bytes = new byte[1024];
        int tmp;
        String chaine;
        String chaine2;
        String mess = null;
        while ((tmp = bis.read(bytes)) != -1) {
            chaine = new String(bytes, 0, tmp);
            chaine2 = chaine;
            mess = chaine.substring(chaine.indexOf("http"),14) + "//random.cat/i" + chaine2.substring(32,chaine2.indexOf("\"}"));
        }
        conn.disconnect();
        return mess;
    }

    private void diplaySongList(TextChannel Salon) {
        Salon.sendTyping();
        String[] TabSong = new String[]{"9000","bite","christmas","débile","han","loveyou","airhorn", "blululu", "chateau", "dégueu",  "hyperfast","mignon","airporn", "bordershock","combat","désolation",
                "hypersad","mystère","aucunsens","boss","cry","explique","héroisme","mégaboss","aurevoir","bruh","damn","fail","idontcare","nani","baka","calme","darkness","falcon","intensefap","nein",
                "batlescouilles","cc","darude", "fap","internet","nipah", "chala" , "cestmort","derp","fast","logique","no","gameover","gg","grossemerde","fdp","lolelol", "nope", "noticeme","nya","ohoh",
                "ok","ora ","phrase","notpass", "nyaaa","ohyes","omg","parfait","plébéien","poi","popopo","princesse","punch","sad","scream","pokemongo","pourrir","psycho","run","sadhorn",
                "sofresh","splendide","tg","turkeyfap","victory","weed","yamete","surprise","toad","ui","ville","weee","yes","tartarin","trap","uiii","waa","wtf","yolo",};
        String Message = "```";
        Message += "\n+s <soundName>";
        for (int i = 0; i < TabSong.length - 1; ++i) {
            Message += "\n";
            String Ligne = TabSong[i];
            for (int j = TabSong[i].length(); j < 15; ++j)
                Ligne += " ";
            Ligne += TabSong[++i];
            for (int k = TabSong[i].length(); k < 15; ++k)
                Ligne += " ";
            Ligne += TabSong[++i];
            for (int l = TabSong[i].length(); l < 15; ++l)
                Ligne += " ";
            Ligne += TabSong[++i];
            Message += Ligne;
        }
        Message += "```";
        Salon.sendMessage(Message);
    }

    private void connexionSalon(MessageReceivedEvent e) {
        if (djJacksonOn)
            e.getChannel().sendMessage("Je suis deja connecté à un salon.");
        else {
            User auteur = e.getMessage().getAuthor();
            VoiceChannel SalonRequeteDjJackson = null;
            for (int i = 0; i < listeSalonsAudio.size(); ++i) {
                if (listeSalonsAudio.get(i).getUsers().contains(auteur)) {
                    SalonRequeteDjJackson = listeSalonsAudio.get(i);
                    break;
                }
            }
            if (SalonRequeteDjJackson == null)
                e.getChannel().sendMessage("Vous n'êtes pas connecté à un salon vocal.");
            else if (Tools.getListeSalonsBot().get(0).getVoiceChannels().contains(SalonRequeteDjJackson)) {
                audioManage1.openAudioConnection(SalonRequeteDjJackson);
                audioManage1.setConnectTimeout(2000);
                audioManage1.setSendingHandler(djJackson);
                djJackson.setVolume(1);
                djJacksonOn = true;
                activeAudio1 = true;
            }
            else if(Tools.getListeSalonsBot().get(1).getVoiceChannels().contains(SalonRequeteDjJackson)) {
                audioManage2.openAudioConnection(SalonRequeteDjJackson);
                audioManage2.setConnectTimeout(2000);
                audioManage2.setSendingHandler(djJackson);
                djJackson.setVolume(1);
                djJacksonOn = true;
                activeAudio2 = true;
            }
        }
    }

    public void use(MessageReceivedEvent e) {
        if (e.getMessage().getContent().equalsIgnoreCase("ping") && !e.getAuthor().isBot()) {
            e.getChannel().sendTyping();
            e.getChannel().sendMessage("pong");
        }//ping -> pong

        if(e.getMessage().getContent().equals("+cat") && !e.getAuthor().isBot()) {
            if(canDoCommand(e)) {
                e.getChannel().sendTyping();
                try {
                    e.getChannel().sendMessage(fonctionCat());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }

        if (e.getMessage().getContent().equalsIgnoreCase("manger") && !e.getAuthor().isBot()) {
            if(canDoCommand(e)) {
                e.getChannel().sendTyping();
                e.getChannel().sendMessage("http://image.noelshack.com/fichiers/2016/36/1473274355-c6b72360-aa9a-4b91-98a2-ccfcd265eda8.jpg");
            }
        }
        if (e.getMessage().getContent().equalsIgnoreCase("zombie") && !e.getAuthor().isBot()) {
            if(canDoCommand(e)) {
                e.getChannel().sendTyping();
                e.getChannel().sendMessage("http://image.noelshack.com/fichiers/2016/36/1473625734-il-tue-ami-zombie.jpg");
            }
        }
        if (e.getMessage().getContent().equalsIgnoreCase("cookies") && !e.getAuthor().isBot()) {
            if(canDoCommand(e)) {
                e.getChannel().sendTyping();
                e.getChannel().sendMessage("http://image.noelshack.com/fichiers/2016/36/1473274354-1107530.jpg");
            }
        }
        if (e.getMessage().getContent().equalsIgnoreCase("patate") && !e.getAuthor().isBot()) {
            if(canDoCommand(e)) {
                e.getChannel().sendTyping();
                e.getChannel().sendMessage("http://image.noelshack.com/fichiers/2016/36/1473624881-potato.jpeg");
            }
        }
        if (e.getMessage().getContent().equals("+twitch mini") && !e.getAuthor().isBot()) {
            if(canDoCommand(e)) {
                e.getChannel().sendTyping();
                e.getChannel().sendMessage("https://www.twitch.tv/minipasglop");
            }
        }
        if(e.getMessage().getContent().equalsIgnoreCase("doge") && ! e.getAuthor().isBot()) {
            if(canDoCommand(e)) {
                e.getChannel().sendTyping();
                e.getChannel().sendMessage("https://t2.rbxcdn.com/3b59a7004e5f205331b7b523c6f919f5");
            }
        }
        if(e.getMessage().getContent().equals("\\triforce") && !e.getAuthor().isBot()) {
            if(canDoCommand(e)) {
                e.getChannel().sendTyping();
                e.getChannel().sendMessage("NewFags can't triforce \n \u200C\u200C \u200C\u200C \u200C\u200C ▲\n" + " ▲ ▲");
            }
        }
        if (e.getMessage().getContent().equalsIgnoreCase("+site b4") && !e.getAuthor().isBot()) {
            if(canDoCommand(e)) {
                e.getChannel().sendTyping();
                e.getChannel().sendMessage("https://www.b4rb4m.fr");
            }
        }//DiversLiens

        if (e.getMessage().getContent().equals("On leur apprends la vie Jackson ?") && e.getAuthor().getId().equals("218461869617184768")) {
            fonctionSpam(e);
        }//Spam

        if (e.getMessage().getContent().equals("+clear") && !e.getAuthor().isBot()) {
            clearChat(e);
        }// On clear le TextChannel

        if (e.getMessage().getContent().contains("+cleanup") && !e.getAuthor().isBot() && e.getMessage().getContent().length() <= 11) {
            clearXMessages(e);
        }// on efface les X derniers messages avec +cleanup X

        if(e.getMessage().getContent().equals("pd") && !e.getAuthor().isBot()){
            e.getChannel().sendMessage("Cawak.");
        }//pd cawak

           /* if (e.getMessage().getContent().contains("@") && !e.getAuthor().isBot()) {
                e.getChannel().sendTyping();
                e.getChannel().sendMessage("Dis donc " + getMentionFromMess(e.getMessage()) + " tu es bien populaire...");
                nombreCommandes++;
            }//Mention depuis le message*/

        if (e.getMessage().getContent().contains("windows") && e.getMessage().getContent().contains("bien")) {
            e.getChannel().sendTyping();
            e.getChannel().sendMessage(Tools.getMentionFromUser(e.getMessage().getAuthor()) + " tu es un sale con.");
        }//insulte les fanboys de windows ^^

        if (e.getMessage().getContent().startsWith("+s") && e.getMessage().getContent().length() > 3 && !e.getMessage().getContent().contains("b4")) {
            if(! djJacksonOn) connexionSalon(e);
            if (djJackson.isPlaying()) {
                e.getChannel().sendMessage("Je suis deja en train de jouer du son groooos");
            } else {
                URL lien = null;
                djJackson.reset();
                try {
                    String buf = e.getMessage().getContent().substring(3);
                    lien = new File("localtracks/"+buf+"/"+buf+".mp3").toURI().toURL();
                } catch (MalformedURLException e1) {
                    e1.printStackTrace();
                }
                try {
                    djJackson.setAudioUrl(lien);
                } catch (IOException | UnsupportedAudioFileException e1) {
                    e1.printStackTrace();
                }
                djJackson.play();
            }
        }//Gestion des chansons

        if (activeAudio1 && djJacksonOn && e.getMessage().getContent().equalsIgnoreCase("+tg")) {
            audioManage1.closeAudioConnection();
            djJackson.reset();
            djJacksonOn  = false;
            activeAudio1 = false;
        }//Couper Jackson et le faire deco.

        if (activeAudio2 && djJacksonOn && e.getMessage().getContent().equalsIgnoreCase("+tg")) {
            audioManage2.closeAudioConnection();
            djJackson.reset();
            djJacksonOn  = false;
            activeAudio2 = false;
        }//Couper Jackson et le faire deco.


       if(e.getMessage().getContent().equals("+s")) {
           diplaySongList(e.getTextChannel());
       }

        if (e.getMessage().getContent().equals("CList") && !e.getAuthor().isBot()) {
            displayList(e.getTextChannel());
        }//Help

    }// Partie relative au listener sur les messages postés dans le chat TEXTUEL
}
