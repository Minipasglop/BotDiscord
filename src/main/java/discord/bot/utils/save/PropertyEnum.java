package discord.bot.utils.save;

public enum PropertyEnum {
    AUTOROLE("autoRole","Role granted to user on server join"),
    USER_EVENT_CHANNEL("userEventChannel","Channel where i am supposed to say Hi / Bye"),
    USER_EVENT_TOGGLING("userEventEnabled","Status of the Hi / Bye feature"),
    VOLUME("volume","Last volume set"),
    LOOP("loop", "Loop on track status");

    private String propertyName;
    private String propertyNameForUser;

    PropertyEnum(String propertyName, String propertyNameForUser){
        this.propertyName = propertyName;
        this.propertyNameForUser = propertyNameForUser;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public String getPropertyNameForUser() {
        return propertyNameForUser;
    }
}
