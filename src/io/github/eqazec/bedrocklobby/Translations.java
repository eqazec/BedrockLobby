package io.github.eqazec.bedrocklobby;

import java.lang.reflect.Field;

import org.bukkit.configuration.file.YamlConfiguration;

public class Translations
{
    public static Translation NO_PERMISSION = new Translation("You do not have permission to do this.");
    public static Translation PLAYER_NOT_SEEN_ON_SERVER = new Translation("The player %s has never joined this server.");
    public static Translation PLAYER_NOT_FOUND = new Translation("Error: Player %s not found");
    public static Translation CLOAK_ON = new Translation("&6Everyone is now invisible.");
    public static Translation CLOAK_OFF = new Translation("&6Everyone is visible again.");
    public static Translation CLOAK_LORE = new Translation("&2Toggles visibility");
    public static Translation CLOAK_NAME = new Translation("&6Cloaking Device");
    public static Translation MOTD_ON_JOIN = new Translation("JOIN MOTD");

    public static void load(YamlConfiguration config)
    {
        try
        {
            for (Field field : Translations.class.getFields())
            {
                field.set(null, new Translation(config.getString(field.getName(), ((Translation) field.get(null)).getOriginalString())));
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void save(YamlConfiguration config)
    {
        try
        {
            for (Field field : Translations.class.getFields())
            {
                config.set(field.getName(), ((Translation) field.get(null)).getOriginalString());
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
