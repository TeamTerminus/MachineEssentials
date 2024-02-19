package net.teamterminus.genericnetworks.util.network;

import net.minecraft.nbt.NbtCompound;

public class NetworkData {

    public NetworkData(NbtCompound tag) {
        fromTag(tag);
    }

    public void fromTag(NbtCompound tag) {}

    public NbtCompound toTag(){
        return new NbtCompound();
    }
}
