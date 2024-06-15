package me.notpseudo.lexiclient.core;

/**
 * Represents the types of Crimson Armors and their abilities
 *
 * Credit to SkyblockAddons
 */
public enum CrimsonArmorAbilityStack {

    CRIMSON("Crimson", "Dominus", "ᝐ"),
    TERROR("Terror", "Hydra Strike", "⁑"),
    AURORA("Aurora", "Arcane Vision", "Ѫ"),
    FERVOR("Fervor", "Fervor", "҉");

    private final String armorName;
    private final String abilityName;
    private final String symbol;

    //lombok plugin moment
    private int currentValue = 0;

    CrimsonArmorAbilityStack(String armorName, String abilityName, String symbol) {
        this.armorName = armorName;
        this.abilityName = abilityName;
        this.symbol = symbol;
    }

    public String getArmorName() {
        return armorName;
    }

    public String getAbilityName() {
        return abilityName;
    }

    public String getSymbol() {
        return symbol;
    }

    public int getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(int currentValue) {
        this.currentValue = currentValue;
    }
}
