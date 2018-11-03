package discord.bot.utils.commands;
//Code from https://github.com/thibautbessone
import discord.bot.utils.save.PropertyEnum;
import discord.bot.utils.save.ServerPropertiesManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.ArrayList;

public class ChatCommandParser {

    class CommandAttributes {
        final String raw;
        final String noprefix;
        final String[] split_noprefix;
        final String invoke;
        final String[] args;
        final MessageReceivedEvent event;

        CommandAttributes(String raw, String noprefix, String[] split_noprefix, String invoke, String[] args, MessageReceivedEvent event) {
            this.raw = raw;
            this.noprefix = noprefix;
            this.split_noprefix = split_noprefix;
            this.invoke = invoke;
            this.args = args;
            this.event = event;
        }
    }

    public CommandAttributes parse(String str, MessageReceivedEvent event){
        ArrayList<String> split = new ArrayList<String>();
        String raw = str;
        String noprefix;
        if(ServerPropertiesManager.getInstance().getPropertyOrBlankFromServer(event.getGuild().getId(), PropertyEnum.PREFIX.getPropertyName()).length() == 1){
            noprefix = raw.substring(1);
        }else {
            noprefix = raw.replaceFirst(ServerPropertiesManager.getInstance().getPropertyOrBlankFromServer(event.getGuild().getId(), PropertyEnum.PREFIX.getPropertyName()), "");
        }
        String[] split_noprefix = noprefix.split(" ");
        for(String s: split_noprefix) {split.add(s);}
        String invoke = split.get(0);
        String[] args = new String[split.size() -1];
        split.subList(1, split.size()).toArray(args);
        return new CommandAttributes(raw,noprefix,split_noprefix,invoke,args,event);
    }
}
