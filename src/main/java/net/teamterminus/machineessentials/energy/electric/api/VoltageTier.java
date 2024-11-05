package net.teamterminus.machineessentials.energy.electric.api;

import net.modificationstation.stationapi.api.util.Formatting;
import net.teamterminus.machineessentials.MachineEssentials;

import java.util.List;

/**
 * Voltage tiers from ULV (8V) to MAX (65535V), with their colors and names.
 */
public record VoltageTier(String voltageLangKey, int minVoltage, int maxVoltage, Formatting textColor, int color) {

	public static final VoltageTier ULV = new VoltageTier("voltage.ulv.name",1,8, Formatting.GRAY, 0x555555);
	public static final VoltageTier LV = new VoltageTier("voltage.lv.name",9,32, Formatting.RED,0xFF5555);
	public static final VoltageTier MV = new VoltageTier("voltage.mv.name",33,128, Formatting.GOLD,0xFFAA00);
	public static final VoltageTier HV = new VoltageTier("voltage.hv.name",129,512, Formatting.YELLOW,0xFFFF55);
	public static final VoltageTier EV = new VoltageTier("voltage.ev.name",513,2048, Formatting.GREEN,0x55FF55);
	public static final VoltageTier UV = new VoltageTier("voltage.uv.name",2048,8192, Formatting.AQUA,0x8C0000);
	public static final VoltageTier OV = new VoltageTier("voltage.ov.name",8193,32767, Formatting.DARK_PURPLE,0x8C0000);
	public static final VoltageTier MAX = new VoltageTier("voltage.max.name",32768,65535, Formatting.LIGHT_PURPLE,0xFF55FF);

	public static final List<VoltageTier> TIERS = MachineEssentials.listOf(ULV,LV,MV,HV,EV,UV,OV,MAX);

	public static VoltageTier get(int voltage){
		for (VoltageTier tier : TIERS) {
			if(voltage >= tier.minVoltage && voltage <= tier.maxVoltage){
				return tier;
			}
		}
		return null;
	}
}
