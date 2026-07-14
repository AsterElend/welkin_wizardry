package aster.welkin.config;

import aster.welkin.Welkin;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = Welkin.MOD_ID)
public class WelkinConfigData extends PartitioningSerializer.GlobalData {
    @ConfigEntry.Category("Client")
    @ConfigEntry.Gui.TransitiveObject
    ClientConfig clientConfig = new ClientConfig();

     @ConfigEntry.Category("Server")
    @ConfigEntry.Gui.TransitiveObject
    ServerConfig serverConfig = new ServerConfig();




    @Config(name = "Client")
    class ClientConfig implements ConfigData{
        @Comment("Whether sigils render the full particle display or just a static image")
        public boolean FancySigils = true;
    }

    @Config(name = "server")
    class ServerConfig implements ConfigData {

    }
}
