package me.notpseudo.lexiclient.utils;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import static me.notpseudo.lexiclient.LexiClient.mc;

public class ArmorUtils {

    /**
     * Counts the number of pieces of armor a player is wearing from a set
     *
     * @param set String name of the set to search for
     * @return the number of equipped armor with the set name
     */
    public static int countPiecesFromSet(String set) {
        if (mc.thePlayer == null) return 0;
        int count = 0;
        for (int i = 0; i < 4; i++) {
            ItemStack armor = mc.thePlayer.getCurrentArmor(i);
            if (armor == null) continue;
            String id = "";
            if (armor.hasTagCompound()) {
                NBTTagCompound tag = armor.getTagCompound();
                if (tag.hasKey("ExtraAttributes", 10) && tag.hasKey("display", 10)) {
                    NBTTagCompound extra = tag.getCompoundTag("ExtraAttributes");
                    if (extra.hasKey("id", 8)) {
                        id = extra.getString("id");
                        if (id.contains(set)) count++;
                    }
                }
            }
        }
        return count;
    }

}
