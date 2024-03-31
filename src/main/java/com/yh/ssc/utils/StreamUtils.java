package com.yh.ssc.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @program: zeroplus-bds-operator
 * @description: stream utils
 * @author: yehang
 * @create: 2022-01-25 14:24
 **/
public class StreamUtils {

    //split
    public static final String DECAR_SPLIT = "#";

    /**
     * build safe stream
     */
    public static <T> Stream<T> ofNullable(Collection<T> data) {
        return Optional.ofNullable(data).map(Collection::stream).orElse(Stream.empty());
    }

    /**
     * build safe stream
     */
    public static <T> Stream<T> ofNullable(T data) {
        return Optional.ofNullable(data).map(Stream::of).orElse(Stream.empty());
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    /**
     * @Title: listToMap
     * @Description: list to map
     */
    public static <K, V> Map<K, V> listToMap(List<K> keys, List<V> values) {
        return ofNullable(keys).collect(Collectors.toMap(key -> key, key -> values.get(keys.indexOf(key))));
    }

    /**
     * return list Descartes
     *
     * @param lists
     * @return
     */
    public static List<String> descartes(List<String>... lists) {
        List<String> tempList = new ArrayList<>();
        for (List<String> list : lists) {
            if (tempList.isEmpty()) {
                tempList = list;
            } else {
                tempList = tempList.stream().flatMap(item -> list.stream().map(item2 -> item + DECAR_SPLIT + item2))
                    .collect(
                        Collectors.toList());
            }
        }
        //filter null result
        return ofNullable(tempList).filter(x -> Objects.nonNull(x)).collect(Collectors.toList());
    }

}
