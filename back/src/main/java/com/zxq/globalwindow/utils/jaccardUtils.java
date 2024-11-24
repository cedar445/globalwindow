package com.zxq.globalwindow.utils;

import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

public class jaccardUtils {

    public static double calculateJaccardSimilarity(Set<String> set1, Set<String> set2) {
        Set<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);

        Set<String> union = new HashSet<>(set1);
        union.addAll(set2);

        return (double) intersection.size() / union.size();
    }
}
