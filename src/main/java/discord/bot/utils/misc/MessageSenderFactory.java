package discord.bot.utils.misc;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import org.apache.log4j.Logger;

import java.io.File;

public class MessageSenderFactory {


    private static MessageSenderFactory instance;
    private static Logger logger = Logger.getLogger(MessageSenderFactory.class);

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
            chan.sendMessage(message).queue(null, throwable -> logger.error("Couldn't send message to : " + chan.getName() + " on server : " + chan.getGuild().getId() + " with message : " + message));
        }
    }

    public void sendSafeMessage(TextChannel chan, MessageEmbed message){
        if(chan.canTalk()){
            chan.sendMessage(message).queue(null, throwable -> logger.error("Couldn't send message to : " + chan.getName() + " on server : " + chan.getGuild().getId() + " with message : " + message));
        }
    }


    public void sendSafePrivateMessage(User author, String message, TextChannel callbackChannel, String callBackMessage){
        if(!author.isBot()) {
            author.openPrivateChannel().queue((privateChannel -> privateChannel.sendMessage(message).queue(null, throwable -> sendSafeMessage(callbackChannel, callBackMessage))));
        }

    }

    public void sendSafePrivateMessage(User author, MessageEmbed message, TextChannel callbackChannel, String callBackMessage){
        if(!author.isBot()) {
            author.openPrivateChannel().queue((privateChannel -> privateChannel.sendMessage(message).queue(null, throwable -> sendSafeMessage(callbackChannel, callBackMessage))));
        }
    }

    public void sendSafeFile(TextChannel channel, File image) {
        if(channel.canTalk()){
            channel.sendFile(image).queue(null, throwable -> logger.error("Couldn't send message to : " + channel.getName() + " on server : " + channel.getGuild().getId()));
        }
    }

}
