package discord.bot.utils.misc;

import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

public class MessageSenderFactory {

    private static MessageSenderFactory instance;

    public static MessageSenderFactory getInstance(){
        if(instance == null){
            instance = new MessageSenderFactory();
        }
        return instance;
    }

    private MessageSenderFactory(){
    }

    public void sendSafeMessage(TextChannel chan, String message){
        if(chan.canTalk()){
            chan.sendMessage(message).queue();
        }
    }

    public void sendSafeMessage(TextChannel chan, MessageEmbed message){
        if(chan.canTalk()){
            chan.sendMessage(message).queue();
        }
    }

    public void sendSafePrivateMessage(User author, String message){
        if(!author.isBot()) {
            author.openPrivateChannel().queue((privateChannel -> privateChannel.sendMessage(message).queue()));
        }
    }

    public void sendSafePrivateMessage(User author, MessageEmbed message){
        if(!author.isBot()) {
            author.openPrivateChannel().queue((privateChannel -> privateChannel.sendMessage(message).queue()));
        }
    }

}
