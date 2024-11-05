package net.teamterminus.machineessentials.energy.electric.api;

public interface IVoltageTiered {

	VoltageTier getTier();

	default VoltageTier getTier(IVoltageTiered block){
		return block.getTier();
	}
}
