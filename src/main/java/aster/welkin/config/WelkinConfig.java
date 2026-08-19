package aster.welkin.config;

import aster.welkin.Welkin;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = Welkin.MOD_ID)
public class WelkinConfig implements ConfigData {
    @Comment("Whether sigils render the full particle display or just a static image")
  public boolean FancySigils = true;
    @Comment("Whether beholding too many sigils sets you on fire")
  public boolean BurnySigils = true;




}
