package discord.bot.utils.misc;

public enum SharedStringEnum {

    NOT_ALLOWED("You're not allowed to do this ! Check your permissions"),
    NO_PRIVATE_CHANNEL_OPEN("I can't send you a DM ! Open your DM first."),
    BOT_OWNER_ONLY("You must be the bot owner in order of doing this !"),
    MISSING_PERMISSIONS("I am missing the permission for doing this!");

    private String sharedString;

    SharedStringEnum(String sharedString) {
        this.sharedString = sharedString;
    }

    public String getSharedString() {
        return this.sharedString;
    }
}
