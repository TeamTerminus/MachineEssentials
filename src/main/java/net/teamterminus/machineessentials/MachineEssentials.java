package net.teamterminus.machineessentials;

import com.mojang.datafixers.util.Pair;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.event.mod.InitEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.Entrypoint;
import net.modificationstation.stationapi.api.util.Namespace;
import net.modificationstation.stationapi.api.util.Null;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class MachineEssentials {

    @Entrypoint.Namespace
    public static final Namespace NAMESPACE = Null.get();

    @Entrypoint.Logger
    public static final Logger LOGGER = Null.get();

    @Entrypoint.Instance
    public static final MachineEssentials INSTANCE = Null.get();

    @EventListener
    private static void init(InitEvent event) {
        LOGGER.info("Machine Essentials initialized.");
    }

    public static <K,V> Map<K,V> mapOf(K[] keys, V[] values){
        if(keys.length != values.length){
            throw new IllegalArgumentException("Arrays differ in size!");
        }
        HashMap<K,V> map = new HashMap<>();
        for (int i = 0; i < keys.length; i++) {
            map.put(keys[i],values[i]);
        }
        return map;
    }

    public static <T,V> T[] arrayFill(T[] array,V value){
        Arrays.fill(array,value);
        return array;
    }

    public static double map(double valueCoord,
                             double startCoord1, double endCoord1,
                             double startCoord2, double endCoord2) {

        final double EPSILON = 1e-12;
        if (Math.abs(endCoord1 - startCoord1) < EPSILON) {
            throw new ArithmeticException("Division by 0");
        }

        double ratio = (endCoord2 - startCoord2) / (endCoord1 - startCoord1);
        return ratio * (valueCoord - startCoord1) + startCoord2;
    }

    @SafeVarargs
    public static <T> List<T> listOf(T... values){
        return new ArrayList<>(Arrays.asList(values));
    }

    @SafeVarargs
    public static <T> Set<T> setOf(T... values){
        return new HashSet<>(Arrays.asList(values));
    }

    public static <T,U> List<Pair<T,U>> zip(List<T> first, List<U> second){
        List<Pair<T,U>> list = new ArrayList<>();
        List<?> shortest = first.size() < second.size() ? first : second;
        for (int i = 0; i < shortest.size(); i++) {
            list.add(Pair.of(first.get(i),second.get(i)));
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
            if(value < min){
                min = value;
            }
        }
        return min;
    }

}
