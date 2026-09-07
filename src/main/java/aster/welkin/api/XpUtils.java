package aster.welkin.api;

import net.minecraft.entity.player.PlayerEntity;

public class XpUtils {
    /**
     * Returns the player's total current XP points.
     * Minecraft stores XP as a combination of full levels + progress toward the next level.
     */
    public static int getPlayerXP(PlayerEntity player) {
        // Calculate total XP from completed levels
        int totalXP = getLevelXP(player.experienceLevel);
        // Add the partial progress toward the next level
        totalXP += Math.round(player.experienceProgress * player.getNextLevelExperience());
        return totalXP;
    }

    /**
     * Returns the total XP points required to reach a given level from zero.
     * Based on Minecraft's official leveling formula.
     */
    private static int getLevelXP(int level) {
        if (level <= 16) {
            return level * level + 6 * level;
        } else if (level <= 31) {
            return (int) (2.5 * level * level - 40.5 * level + 360);
        } else {
            return (int) (4.5 * level * level - 162.5 * level + 2220);
        }
    }

    /**
     * Subtracts a given number of XP points from a player.
     * Clamps at 0 — will not go into negative XP.
     *
     * @param player   The player to subtract XP from.
     * @param amount   The number of XP points to remove.
     */
    public static void subtractPlayerXP(PlayerEntity player, int amount) {
        int currentXP = getPlayerXP(player);
        int newXP = Math.max(0, currentXP - amount);

        // Reset the player's XP to 0, then add back the remainder
        player.experienceLevel = 0;
        player.experienceProgress = 0f;
        player.totalExperience = 0;
        player.addExperience(newXP);
    }
}
