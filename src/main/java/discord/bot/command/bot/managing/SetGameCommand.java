package discord.bot.command.bot.managing;

import discord.bot.BotGlobalManager;
import discord.bot.command.ICommand;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class SetGameCommand implements ICommand {

    private String HELP = "This command allows the Owner to change the current game status of the bot. \nUsage: `!setGame type game`";
    private String COMMAND_SUCCESS = "Successfully updated game.";
    private String NOT_ALLOWED = "You must be the bot Owner in order to do that!";
    private String WRONG_TYPE = "Wrong type specified bruh :cold_sweat:";

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        if (args.length != 0 && args[0].equals("help") || args.length < 2) {
            return false;
        } else return true;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        if(event.getAuthor().getId().equals(BotGlobalManager.getConfig().getBotOwnerUserId())) {
            String type = args[0];
            String game = "";
            for(int i = 1; i  < args.length; ++i){
                game += args[i] + (i+1 < args.length ? " " : "");
            }
            switch(type){
                case "playing":
                    event.getJDA().getPresence().setGame(Game.playing(game));
                    break;
                case "watching":
                    event.getJDA().getPresence().setGame(Game.watching(game));
                    break;
                case "listening":
                    event.getJDA().getPresence().setGame(Game.listening(game));
                    break;
                default:
                    event.getAuthor().openPrivateChannel().queue((privateChannel -> privateChannel.sendMessage(WRONG_TYPE).queue()));
            }
            event.getAuthor().openPrivateChannel().queue((privateChannel -> privateChannel.sendMessage(COMMAND_SUCCESS).queue()));
        }else {
            event.getMessage().delete().queue();
            event.getAuthor().openPrivateChannel().queue((privateChannel -> privateChannel.sendMessage(NOT_ALLOWED).queue()));
        }
    }

    @Override
    public String help() {
        return HELP;
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {
        if (!success) {
            event.getTextChannel().sendMessage(help()).queue();
        }
    }
}
