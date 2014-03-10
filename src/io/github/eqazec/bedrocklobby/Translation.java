package io.github.eqazec.bedrocklobby;

import org.bukkit.ChatColor;

public class Translation
{
    private final String originalString;

    public Translation(String string)
    {
        this.originalString = string;
    }

    public String getOriginalString()
    {
        return this.originalString;
    }

    public boolean isEmpty()
    {
        return this.originalString.isEmpty();
    }

    public String toString()
    {
        return ChatColor.translateAlternateColorCodes('&', this.originalString);
    }

    public String toString(Object... args)
    {
        return ChatColor.translateAlternateColorCodes('&', String.format(this.originalString, args));
    }

}
