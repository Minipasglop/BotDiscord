package discord.bot.command.server.managing;

import discord.bot.command.ICommand;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.List;

public class MuteCommand extends ICommand {

    private final String HELP = "Mute one / many users on the server. \nUsage : `!"+ this.commandName +" @user1 @userB Reason duration(minutes)`";
    private final String MUTE_MESSAGE = "You've been muted because of : ";
    private final String UNMUTE_MESSAGE = "You've been unmuted. Stay still now ;) !";
    private final String NOT_ALLOWED = "You're not allowed to mute other users... Sadly:)";
    private final String ACTION_PERFORMED = "Rendre muet : ";
    private final String ACTION_CALLBACK = "Rendre parole possible : ";

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
            List<Member> targetedUsers = event.getMessage().getMentionedMembers();
            for (Member curr : targetedUsers) {
                event.getGuild().getController().setMute(curr, true).queue();
                System.out.println(ACTION_PERFORMED + curr.getUser().getName() + " sur le serveur : " + event.getGuild().getName());
                PrivateChannel chanToTalk = curr.getUser().openPrivateChannel().complete();
                chanToTalk.sendMessage(MUTE_MESSAGE + args[args.length - 2] + " et ce pour " + args[args.length - 1] + " minute." + (Integer.parseInt(args[args.length - 1]) > 1 ? "s" : "")).queue();
                Runnable waitUntilDemute = () -> {
                    try {
                        Thread.sleep(Integer.parseInt(args[args.length - 1]) * 60000);
                        event.getGuild().getController().setMute(curr, false).queue();
                        System.out.println(ACTION_CALLBACK + curr.getUser().getName() + " sur le serveur : " + event.getGuild().getName());
                        chanToTalk.sendMessage(UNMUTE_MESSAGE).queue();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                };
                waitUntilDemute.run();
            }
        } else {
            event.getMessage().delete().queue();
            PrivateChannel chanToTalk = event.getAuthor().openPrivateChannel().complete();
            chanToTalk.sendMessage(NOT_ALLOWED).queue();
        }
    }

    @Override
    public String help() {
        return HELP;
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {
        if(!success) {
            event.getTextChannel().sendMessage(help()).queue();
        }
    }

}
