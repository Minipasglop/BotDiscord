package discord.bot.command.server.managing;

import discord.bot.command.ICommand;
import discord.bot.utils.misc.MessageSenderFactory;
import discord.bot.utils.misc.SharedStringEnum;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.util.List;

public class MoveCommand extends ICommand {

    private final String HELP = "Moves an user to another VocalChannel if he is connected to any of the server. \nUsage : `!"+ this.commandName +" @User TargettedChannel `";
    private final String COMMAND_FAILED = "Something unexpected happened. Please make sure the user is already connected to a vocal channel.";
    private final String REQUIRE_MENTIONED_USERS = "You must mention the users you want to move";
    private final String ACTION_PERFORMED = "Déplacer : ";
    private static Logger logger = Logger.getLogger(MoveCommand.class);

    public MoveCommand(String commandName) {
        super(commandName);
    }

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        if(args.length != 0 && args[0].equals("help") || args.length < 2) return false;
        else return true;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        if(event.getMember().getPermissions().contains(Permission.VOICE_MOVE_OTHERS)) {
            if(!event.getGuild().getSelfMember().getPermissions().contains(Permission.VOICE_MOVE_OTHERS)){
                MessageSenderFactory.getInstance().sendSafeMessage(event.getTextChannel(), SharedStringEnum.MISSING_PERMISSIONS.getSharedString());
                return;
            }
            if(!event.getMessage().getMentionedMembers().isEmpty()) {
                List<Member> targetedUsers = event.getMessage().getMentionedMembers();
                List<VoiceChannel> targetChannel = event.getGuild().getVoiceChannelsByName(args[args.length - 1], true);
                for (Member curr : targetedUsers) {
                    try {
                        event.getGuild().getController().moveVoiceMember(curr, targetChannel.get(0)).queue();
                        logger.log(Level.INFO, ACTION_PERFORMED + curr.getUser().getName() + " vers le salon " + targetChannel.get(0).getName() + " sur le serveur : " + event.getGuild().getName());
                    } catch (Exception e) {
                        logger.log(Level.ERROR, event.getMessage(), e);
                        MessageSenderFactory.getInstance().sendSafeMessage(event.getTextChannel(), COMMAND_FAILED);
                    }
                }
            }else {
                MessageSenderFactory.getInstance().sendSafeMessage(event.getTextChannel(), REQUIRE_MENTIONED_USERS);
            }
        }else {
            event.getMessage().delete().queue();
            MessageSenderFactory.getInstance().sendSafePrivateMessage(event.getAuthor(), SharedStringEnum.NOT_ALLOWED.getSharedString(), event.getTextChannel(), SharedStringEnum.NOT_ALLOWED.getSharedString());
        }
    }

    @Override
    public String help() {
        return HELP;
    }

}
