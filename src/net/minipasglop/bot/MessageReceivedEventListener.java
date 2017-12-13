package net.minipasglop.bot;

import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.managers.AudioManager;
import net.dv8tion.jda.core.requests.RequestFuture;
import net.dv8tion.jda.core.requests.restaction.pagination.MessagePaginationAction;

import javax.swing.*;
import java.io.BufferedInputStream;
import java.io.IOException;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;


public class MessageReceivedEventListener {

    private MyTimer monTimer;
    private Map<Guild,AudioManager> audioManagers;
    /*private Map<Guild,MyUrlPlayer> lesDjJacksons;
    private List<VoiceChannel> listeSalonsAudio;*/

    public MessageReceivedEventListener() throws MalformedURLException {
        monTimer = new MyTimer();
        /* listeSalonsAudio = Main.getJda().getVoiceChannels();
        audioManagers = new HashMap<>();
        lesDjJacksons = new HashMap<>();
        for (Guild chan : Main.getListeSalonBot()){
            audioManagers.put(chan, Main.getJda().getGuildById(chan.getId()).getAudioManager());
            lesDjJacksons.put(chan,new MyUrlPlayer(Main.getJda()));
        }*/
    }

    private void fonctionSpam(MessageReceivedEvent e) {
        int cpt = 0;
        Main.setSpam(true);
        long timer;
        while (cpt < 10000) {
            if (!Main.isSpam())
                return;
            e.getChannel().sendTyping().complete();
            e.getChannel().sendMessage("Je suis jackson le gentil poulet. Rejoins la jackson family ! : http://steamcommunity.com/groups/Jacksonity ").complete();
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
        List<Message> historique = e.getTextChannel().getHistory().retrievePast(100).complete();
        if(historique.size() == 0 ){
            e.getTextChannel().sendTyping().complete();
            e.getChannel().sendMessage("La salle de chat a été nettoyée. :see_no_evil: ").queue();
            return;
        }
        Iterator<Message> iterator = historique.iterator();
        while(iterator.hasNext()){
            iterator.next().delete().queue();
        }
        e.getTextChannel().deleteMessages(historique).complete();
        clearChat(e);
    }

    private static void clearXMessages(MessageReceivedEvent e) {
        String message = e.getMessage().getContent();
        int nombre;
        String entier = message.substring(9);
        nombre = Integer.parseInt(entier) +1;
        List<Message> historique = e.getTextChannel().getHistory().retrievePast(nombre).complete();
        e.getTextChannel().deleteMessages(historique).queue();
        e.getTextChannel().sendTyping().queue();
        e.getChannel().sendMessage((nombre - 1) + " messages ont été supprimés. ( +cleanup " + (nombre - 1) + " )").queue();
    }// On efface X messages

    private void displayList(TextChannel Salon) {
        Salon.sendTyping().complete();
        String[] TabCommandes = new String[]{"+s", "+twitch mini", "+site b4", "+clear" , "+cat", "+cleanup XX ", "+help ","+tg"};
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
        Salon.sendMessage(Message).complete();
    }//Affiche la liste des commandes. Penser à mettre à jour à l'ajout de new commandes.

    private boolean canDoCommand(MessageReceivedEvent e) {
        if (monTimer.getTempsRestant() != 0) {
            e.getChannel().sendTyping().complete();
            e.getChannel().sendMessage(Tools.getMentionFromUser(e.getAuthor()) + " ferme ta gueule.").complete();
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
        String chaine, chaine2;
        String mess = null;
        while ((tmp = bis.read(bytes)) != -1) {
            chaine = new String(bytes, 0, tmp);
            chaine2 = chaine;
            mess = chaine.substring(chaine.indexOf("http"),14) + "//random.cat/i" + chaine2.substring(32,chaine2.indexOf("\"}"));
        }
        conn.disconnect();
        return mess;
    }

    /*private void diplaySongList(TextChannel Salon) {
        Salon.sendTyping();
        String[] nomSongs = new File("localtracks/").list();
        String Message ="```\n+s <soundName>";
        int cpt = 0;
        for(String path : nomSongs) {
            if(cpt % 3 == 0)
                Message += "\n";
            String line = path;
            for (int j = path.length(); j < 20; ++j)
                    line += " ";
            Message += line;
            cpt++;
        }
        Message += "```";
        Salon.sendMessage(Message);
    }

    private void connexionSalon(MessageReceivedEvent e) {
        if (lesDjJacksons.get(e.getGuild()).isPlaying())
            e.getChannel().sendMessage("Je suis deja connecté à un salon.");
        else {
            VoiceChannel SalonRequeteDjJackson = null;
            for (int i = 0; i < listeSalonsAudio.size(); ++i) {
                if (listeSalonsAudio.get(i).getUsers().contains(e.getMessage().getAuthor())) {
                    SalonRequeteDjJackson = listeSalonsAudio.get(i);
                    break;
                }
            }
            if (SalonRequeteDjJackson == null)
                e.getChannel().sendMessage("Vous n'êtes pas connecté à un salon vocal.");
            else if (e.getGuild().getVoiceChannels().contains(SalonRequeteDjJackson)) {
                audioManagers.get(e.getGuild()).openAudioConnection(SalonRequeteDjJackson);
                audioManagers.get(e.getGuild()).setConnectTimeout(2000);
                audioManagers.get(e.getGuild()).setSendingHandler(lesDjJacksons.get(e.getGuild()));
                lesDjJacksons.get(e.getGuild()).setVolume(1);
            }
        }
    }

    private boolean songExist(String songName){
        String[] nomSongs = new File("localtracks/").list();
        for(String path : nomSongs)
            if(songName.equals(path))
                return true;
        return false;
    } */

    public void use(MessageReceivedEvent e) {
        if (e.getMessage().getContent().equalsIgnoreCase("ping") && !e.getAuthor().isBot())
            e.getChannel().sendMessage("Le ping est de : " + String.valueOf(Main.getJda().getPing()) + " ms").queue();
        //ping -> pong

        else if(e.getMessage().getContent().equals("+cat") && !e.getAuthor().isBot()) {
            if(canDoCommand(e)) {
                try {
                    e.getChannel().sendMessage(fonctionCat()).queue();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }

        else if (e.getMessage().getContent().equals("+twitch mini") && !e.getAuthor().isBot()) {
            if(canDoCommand(e)) {
                e.getChannel().sendMessage("https://www.twitch.tv/minipasglop").queue();
            }
        }

        else if(e.getMessage().getContent().equalsIgnoreCase("doge") && ! e.getAuthor().isBot()) {
            if(canDoCommand(e)) {
                e.getChannel().sendMessage("https://t2.rbxcdn.com/3b59a7004e5f205331b7b523c6f919f5").queue();
            }
        }

        else if(e.getMessage().getContent().equals("\\triforce") && !e.getAuthor().isBot()) {
            if(canDoCommand(e)) {
                e.getChannel().sendMessage("NewFags can't triforce \n \u200C\u200C \u200C\u200C \u200C\u200C ▲\n" + " ▲ ▲").queue();
            }
        }

        else if (e.getMessage().getContent().equalsIgnoreCase("+site b4") && !e.getAuthor().isBot()) {
            if(canDoCommand(e)) {
                e.getChannel().sendMessage("https://www.b4rb4m.fr").queue();
            }
        }//DiversLiens

        else if (e.getMessage().getContent().equals("On leur apprends la vie Jackson ?") && e.getAuthor().getId().equals("218461869617184768")) {
            fonctionSpam(e);
        }//Spam

        else if (e.getMessage().getContent().equals("+clear") && !e.getAuthor().isBot()) {
            clearChat(e);
        }// On clear le TextChannel

        else if (e.getMessage().getContent().contains("+cleanup") && !e.getAuthor().isBot() && e.getMessage().getContent().length() <= 11) {
            clearXMessages(e);
        }// on efface les X derniers messages avec +cleanup X

        else if(e.getMessage().getContent().equals("pd") && !e.getAuthor().isBot()){
            e.getChannel().sendMessage("Cawak.").queue();
        }//pd cawak

        else if (e.getMessage().getContent().contains("windows") && e.getMessage().getContent().contains("bien")) {
            e.getChannel().sendMessage(Tools.getMentionFromUser(e.getMessage().getAuthor()) + " tu es un sale con.").queue();
        }//insulte les fanboys de windows ^^

        /* if (e.getMessage().getContent().startsWith("+s") && e.getMessage().getContent().length() > 3  && !e.getAuthor().isBot()) {
            boolean songExist = songExist(e.getMessage().getContent().substring(3));
            if(! audioManagers.get(e.getGuild()).isConnected() && songExist) connexionSalon(e);
            if (lesDjJacksons.get(e.getGuild()).isPlaying()) {
                e.getChannel().sendMessage("Je suis deja en train de jouer du son groooos");
            } else if(songExist){
                URL lien = null;
                lesDjJacksons.get(e.getGuild()).reset();
                try {
                    String buf = e.getMessage().getContent().substring(3);
                    lien = new File("localtracks/"+buf+"/"+buf+".mp3").toURI().toURL();
                } catch (MalformedURLException e1) {
                    e1.printStackTrace();
                }
                try {
                    lesDjJacksons.get(e.getGuild()).setAudioUrl(lien);
                    lesDjJacksons.get(e.getGuild()).play();
                } catch (IOException | UnsupportedAudioFileException e1) {
                    e1.printStackTrace();
                }

            }
            else
                e.getChannel().sendMessage("SoundName pas valable groooos :/");
        }//Gestion des chansons

        if (audioManagers.get(e.getGuild()).isConnected() && e.getMessage().getContent().equalsIgnoreCase("+tg") && !e.getAuthor().isBot()) {
            lesDjJacksons.get(e.getGuild()).stop();
            lesDjJacksons.get(e.getGuild()).reset();
            audioManagers.get(e.getGuild()).closeAudioConnection();
        }//Couper Jackson et le faire deco.

       if(e.getMessage().getContent().equals("+s")) {
           diplaySongList(e.getTextChannel());
       }//La liste des commandes */

        if (e.getMessage().getContent().equals("+help") && !e.getAuthor().isBot()) {
            displayList(e.getTextChannel());
        }//Help

    }// Partie relative au listener sur les messages postés dans le chat TEXTUEL
}
