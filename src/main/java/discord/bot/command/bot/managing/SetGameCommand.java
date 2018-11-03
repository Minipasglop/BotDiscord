package discord.bot.command.bot.managing;

import discord.bot.BotGlobalManager;
import discord.bot.command.ICommand;
import discord.bot.utils.misc.MessageSenderFactory;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.List;

public class SetGameCommand extends ICommand {

    private String HELP = "This command allows the Owner to change the current game status of the bot. \nUsage: `!" + this.commandName + " type game`";
    private String COMMAND_SUCCESS = "Successfully updated game.";
    private String NOT_ALLOWED = "You must be the bot Owner in order to do that!";
    private String WRONG_TYPE = "Wrong type specified bruh :cold_sweat:";

    public SetGameCommand(String commandName) {
        super(commandName);
    }

    private boolean setGameWithType(String type, String gameToSet){
        boolean success = true;
        Game game = null;
        List<JDA> shards = BotGlobalManager.getShards();
        switch(type){
            case "playing":
                game = Game.playing(gameToSet);
                break;
            case "watching":
                game = Game.watching(gameToSet);
                break;
            case "listening":
                game = Game.listening(gameToSet);
                break;
            default:
                success = false;
        }
        if(game != null){
            for(int i = 0; i < shards.size(); i++){
                shards.get(i).getPresence().setGame(game);
            }
        }
        return success;
    }

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
            if(!setGameWithType(type,game)){
                MessageSenderFactory.getInstance().sendSafePrivateMessage(event.getAuthor(),WRONG_TYPE);
                return;
            }
            MessageSenderFactory.getInstance().sendSafePrivateMessage(event.getAuthor(),COMMAND_SUCCESS);
        }else {
            event.getMessage().delete().queue();
            MessageSenderFactory.getInstance().sendSafePrivateMessage(event.getAuthor(),NOT_ALLOWED);
        }
    }

    @Override
    public String help() {
        return HELP;
    }

}
