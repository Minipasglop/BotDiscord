package discord.bot.command;

public enum CommandEnum {
    SOUND("play"),
    VOLUME("vol"),
    SKIP("skip"),
    STOP("stop"),
    LOOP("loop"),
    QUEUE("queue"),
    PLAY_PAUSE("p"),
    SHUFFLE("shuffle"),
    ADD_ROLE("addRole"),
    SET_AUTO_ROLE_ON_JOIN("setAutoRole"),
    USER_EVENT_CHANNEL("greetingschannel"),
    USER_EVENT_TOGGLING("greetingsmessage"),
    BAN("ban"),
    HELP("help"),
    KICK("kick"),
    MOVE("move"),
    MUTE("mute"),
    PURGE("purge"),
    INFO("info"),
    SERVER_SETTINGS("state"),
    PING("ping"),
    YOUTUBE_VIDEO_LINK("yt"),
    SET_BOT_GAME("setGame"),
    FORCE_PROPERTIES_SAVING("saveProperties");

    private String commandName;

    CommandEnum(String commandName){
        this.commandName = commandName;
    }

    public String getCommandName(){
        return this.commandName;
    }
}
