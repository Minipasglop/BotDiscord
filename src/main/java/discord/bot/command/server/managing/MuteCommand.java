package discord.bot.command.server.managing;

import discord.bot.command.ICommand;
import discord.bot.utils.misc.MessageSenderFactory;
import discord.bot.utils.misc.SharedStringEnum;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.util.List;

public class MuteCommand extends ICommand {

    private final String HELP = "Mute one / many users on the server. \nUsage : `!"+ this.commandName +" @user1 @userB Reason duration(minutes)`";
    private final String MUTE_MESSAGE = "You've been muted because of : ";
    private final String UNMUTE_MESSAGE = "You've been unmuted. Stay still now ;) !";
    private final String REQUIRE_MENTIONED_USERS = "You must mention the users you want to mute";
    private final String ACTION_PERFORMED = "Rendre muet : ";
    private final String ACTION_CALLBACK = "Rendre parole possible : ";
    private static Logger logger = Logger.getLogger(MuteCommand.class);

    public MuteCommand(String commandName) {
        super(commandName);
    }

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        if(args.length < 3 && args[0].equals("help") || args.length < 3 ) return false;
        return true;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        if(event.getMember().getPermissions().contains(Permission.VOICE_MUTE_OTHERS)) {
            if(!event.getGuild().getSelfMember().getPermissions().contains(Permission.VOICE_MUTE_OTHERS)){
                MessageSenderFactory.getInstance().sendSafeMessage(event.getTextChannel(), SharedStringEnum.MISSING_PERMISSIONS.getSharedString());
                return;
            }
            if(!event.getMessage().getMentionedMembers().isEmpty()) {
                List<Member> targetedUsers = event.getMessage().getMentionedMembers();
                for (Member curr : targetedUsers) {
                    event.getGuild().getController().setMute(curr, true).queue();
                    logger.log(Level.INFO, ACTION_PERFORMED + curr.getUser().getName() + " sur le serveur : " + event.getGuild().getName());
                    MessageSenderFactory.getInstance().sendSafePrivateMessage(curr.getUser(), buildMuteMessage(args[args.length - 2], args[args.length - 1]), event.getTextChannel(), MUTE_MESSAGE);
                    Runnable waitUntilDemute = () -> {
                        try {
                            Thread.sleep(Integer.parseInt(args[args.length - 1]) * 60000);
                            event.getGuild().getController().setMute(curr, false).queue();
                            logger.log(Level.INFO, ACTION_CALLBACK + curr.getUser().getName() + " sur le serveur : " + event.getGuild().getName());
                            MessageSenderFactory.getInstance().sendSafePrivateMessage(curr.getUser(), UNMUTE_MESSAGE, event.getTextChannel(), UNMUTE_MESSAGE);
                        } catch (InterruptedException e) {
                            logger.log(Level.ERROR, event.getMessage(), e);
                        }
                    };
                    waitUntilDemute.run();
                }
            }else {
                MessageSenderFactory.getInstance().sendSafeMessage(event.getTextChannel(), REQUIRE_MENTIONED_USERS);
            }
        } else {
            event.getMessage().delete().queue();
            MessageSenderFactory.getInstance().sendSafePrivateMessage(event.getAuthor(), SharedStringEnum.NOT_ALLOWED.getSharedString(), event.getTextChannel(), SharedStringEnum.NOT_ALLOWED.getSharedString());
        }
    }

    private String buildMuteMessage(String reason, String time) {
        return MUTE_MESSAGE + reason + " for " + time + " minute" + (Integer.parseInt(time) > 1 ? "s" : "");
    }

    @Override
    public String help() {
        return HELP;
    }

}
