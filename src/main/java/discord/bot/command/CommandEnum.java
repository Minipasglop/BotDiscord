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
    INVITE("invite"),
    SERVER_SETTINGS("serverconf"),
    PING("ping"),
    YOUTUBE_VIDEO_LINK("yt"),
    CAT_PICTURE("cat"),
    DOG_PICTURE("dog"),
    CHUCK_NORRIS_FACT("chucknorris"),
    SET_BOT_GAME("setGame"),
    FORCE_PROPERTIES_SAVING("saveProperties"),
    SET_PREFIX("prefix");

    private String commandName;

    CommandEnum(String commandName){
        this.commandName = commandName;
    }

    public String getCommandName(){
        return this.commandName;
    }
}
