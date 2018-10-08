package discord.bot.command.bot.info;

import discord.bot.BotGlobalManager;
import discord.bot.command.ICommand;
import discord.bot.utils.misc.MessageSenderFactory;
import discord.bot.utils.misc.UptimeFactory;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.List;

public class InfoCommand extends ICommand {

    private String HELP = "The command `"+ this.commandName +"` displays some infos about the bot status at the moment. \nUsage: `!" + this.commandName + "`";

    public InfoCommand(String commandName) {
        super(commandName);
    }

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        if (args.length != 0 && args[0].equals("help") || args.length != 0) {
            return false;
        } else return true;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        int usersNumber = 0;
        int serversNumber = 0;
        int channelNumber = 0;
        int voiceChannelNumber = 0;
        List<JDA> shards = BotGlobalManager.getShards();
        for(int i = 0; i < shards.size(); i++){
            usersNumber += shards.get(i).getUsers().size();
            serversNumber += shards.get(i).getGuilds().size();
            channelNumber += shards.get(i).getTextChannels().size();
            voiceChannelNumber += shards.get(i).getVoiceChannels().size();
        }

        String uptime = UptimeFactory.getBotUptime();

        EmbedBuilder builder = new EmbedBuilder();
        builder.setAuthor("Info");
        builder.setColor(Color.ORANGE);
        builder.setThumbnail(event.getJDA().getSelfUser().getAvatarUrl());
        builder.addField("Users :busts_in_silhouette:", String.valueOf(usersNumber), true);
        builder.addField("Servers :desktop:", String.valueOf(serversNumber), true);
        builder.addField("Channels :keyboard: :loud_sound:", String.valueOf(channelNumber) + " text / " + String.valueOf(voiceChannelNumber) + " voice channels \n̔̏", true);
        builder.addBlankField(true);
        builder.addField("Uptime :timer:", uptime, true);
        MessageSenderFactory.getInstance().sendSafeMessage(event.getTextChannel(),builder.build());
    }


    @Override
    public String help() {
        return HELP;
    }

}
