package net.teamterminus.machineessentials;

import com.mojang.datafixers.util.Pair;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.modificationstation.stationapi.api.event.mod.InitEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.EntrypointManager;
import net.modificationstation.stationapi.api.util.Namespace;
import net.modificationstation.stationapi.api.util.math.Direction;
import net.modificationstation.stationapi.api.world.StationFlatteningWorld;
import org.apache.logging.log4j.Logger;

import java.lang.invoke.MethodHandles;
import java.util.*;

public class MachineEssentials {
    static {
        EntrypointManager.registerLookup(MethodHandles.lookup());
    }

    @SuppressWarnings("UnstableApiUsage")
    public static final Namespace NAMESPACE = Namespace.resolve();

    public static final Logger LOGGER = NAMESPACE.getLogger("MachineEssentials");

    @EventListener
    private static void init(InitEvent event) {
        LOGGER.info("Machine Essentials initialized.");
    }

    public static <K, V> Map<K, V> mapOf(K[] keys, V[] values){
        if (keys.length != values.length){
            throw new IllegalArgumentException("Arrays differ in size!");
        }
        HashMap<K, V> map = new HashMap<>();
        for (int i = 0; i < keys.length; i++) {
            map.put(keys[i], values[i]);
        }
        return map;
    }

    public static <T, V> T[] arrayFill(T[] array, V value){
        Arrays.fill(array, value);
        return array;
    }

    /**
     * Maps a value from one range to another.
     * @return The resuling value after being mapped from one range to another
     */
    public static double map(double value,
                             double fromMin, double fromMax,
                             double toMin, double toMax) {

        final double EPSILON = 1e-12;
        if (Math.abs(fromMax - fromMin) < EPSILON) {
            throw new ArithmeticException("Division by 0");
        }

        double ratio = (toMax - toMin) / (fromMax - fromMin);
        return ratio * (value - fromMin) + toMin;
    }

    @SafeVarargs
    public static <T> List<T> listOf(T... values){
        return new ArrayList<>(Arrays.asList(values));
    }

    @SafeVarargs
    public static <T> Set<T> setOf(T... values){
        return new HashSet<>(Arrays.asList(values));
    }

    public static <T, U> List<Pair<T, U>> zip(List<T> first, List<U> second){
        List<Pair<T, U>> list = new ArrayList<>();
        List<?> shortest = first.size() < second.size() ? first : second;
        for (int i = 0; i < shortest.size(); i++) {
            list.add(Pair.of(first.get(i), second.get(i)));
        }
        return list;
    }

    /**
     * @param values The values to be checked
     * @return Returns the smallest of <code>values</code>
     */
    public static long multiMin(long... values){
        long min = Long.MAX_VALUE;
        for (long value : values) {
            if (value < min){
                min = value;
            }
        }
        return min;
    }

    public static BlockEntity getBlockEntity(Direction dir, BlockView world, BlockEntity origin){
        return world.getBlockEntity(origin.x + dir.getOffsetX(), origin.y + dir.getOffsetY(), origin.z + dir.getOffsetZ());
    }

    public static Block getBlock(Direction dir, StationFlatteningWorld world, BlockEntity origin){
        return world.getBlockState(origin.x + dir.getOffsetX(), origin.y + dir.getOffsetY(), origin.z + dir.getOffsetZ()).getBlock();
    }

    public static BlockEntity getBlockEntity(Direction dir, BlockView world, BlockPos origin){
        return world.getBlockEntity(origin.x + dir.getOffsetX(), origin.y + dir.getOffsetY(), origin.z + dir.getOffsetZ());
    }

    public static Block getBlock(Direction dir, StationFlatteningWorld world, BlockPos origin){
        return world.getBlockState(origin.x + dir.getOffsetX(), origin.y + dir.getOffsetY(), origin.z + dir.getOffsetZ()).getBlock();
    }
}
