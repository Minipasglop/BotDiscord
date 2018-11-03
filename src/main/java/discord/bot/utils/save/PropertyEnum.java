package discord.bot.utils.save;

public enum PropertyEnum {
    AUTOROLE("autoRole","Role granted to user on server join", ""),
    USEREVENTCHANNEL("userEventChannel","Channel where i am supposed to say Hi / Bye", ""),
    USEREVENTENABLED("userEventEnabled","Status of the Hi / Bye feature", ""),
    VOLUME("volume","Last volume set", "50"),
    LOOP("loop", "Loop on track status", "false"),
    PREFIX("prefix", "Bot's command prefix", "!");

    private String propertyName;
    private String propertyNameForUser;
    private String defaultValue;

    PropertyEnum(String propertyName, String propertyNameForUser, String defaultValue){
        this.propertyName = propertyName;
        this.propertyNameForUser = propertyNameForUser;
        this.defaultValue = defaultValue;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public String getPropertyNameForUser() {
        return propertyNameForUser;
    }

    public String getDefaultValue() {
        return defaultValue;
    }
}
