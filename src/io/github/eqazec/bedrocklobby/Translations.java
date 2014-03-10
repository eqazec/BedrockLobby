package io.github.eqazec.bedrocklobby;

import java.lang.reflect.Field;

import org.bukkit.configuration.file.YamlConfiguration;

public class Translations
{
    public static Translation NO_PERMISSION             = new Translation("You do not have permission to do this.");
    public static Translation PLAYER_NOT_SEEN_ON_SERVER = new Translation("The player %s has never joined this server.");
    public static Translation PLAYER_NOT_FOUND          = new Translation("Error: Player %s not found");

    public static void load(YamlConfiguration config)
    {
        try
        {
            for (Field field : Translations.class.getFields())
            {
                field.set(null, new Translation(config.getString(field.getName(),
                        ((Translation)field.get(null)).getOriginalString())));
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
                config.set(field.getName(), ((Translation)field.get(null)).getOriginalString());
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
