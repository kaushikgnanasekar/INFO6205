package edu.neu.coe.info6205.sort.par;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;

/**
 * This code has been fleshed out by Ziyao Qiao. Thanks very much.
 * CONSIDER tidy it up a bit.
 */
public class Main {

    public static void main(String[] args) {
        processArgs(args);
        System.out.println("Degree of parallelism: " + ForkJoinPool.getCommonPoolParallelism());
        Random random = new Random();
        int[] inputSizes = {32768};
        int[] threadCount = {2, 4, 8, 16, 32};
        ForkJoinPool forkPool;
        int[] inputArray;
        int runs = 10;
        int i = 0;
        while (i < inputSizes.length) {
            int length = inputSizes[i];
            inputArray = new int[length];
            int[] cutoffLengths = {length / 1024 + 1, length / 512 + 1, length / 256 + 1, length / 128 + 1,
                length / 64 + 1, length / 32 + 1, length / 16 + 1, length / 8 + 1, length / 4 + 1,
                length / 2 + 1, length + 1};

            for (int j = 0; j < cutoffLengths.length; j++) {
                ParSort.cutoff = cutoffLengths[j];
                for (int k = 0; k < threadCount.length; k++) {
                    forkPool = new ForkJoinPool(threadCount[k]);
                    double startTime = System.nanoTime();
                    for (int l = 0; l < runs; l++) {
                        for (int m = 0; m < length; m++) inputArray[m] = random.nextInt(10000000);
                        ParSort.sort(inputArray, 0, inputArray.length, forkPool);
                    }
                    double endTime = System.nanoTime();
                    double avgTimeTakenInMs = ((endTime - startTime) / runs) * Math.pow(10, -6); 
                    System.out.println("Array size=" + length + " Cut-Off=" + ParSort.cutoff + " Thread(t)=" + threadCount[k] + " Average Time(ms)=" + avgTimeTakenInMs);
                }
            }
            i++;
        }
    }

    private static void processArgs(String[] args) {
        String[] xs = args;
        while (xs.length > 0)
            if (xs[0].startsWith("-")) xs = processArg(xs);
    }

    private static String[] processArg(String[] xs) {
        String[] result = new String[0];
        System.arraycopy(xs, 2, result, 0, xs.length - 2);
        processCommand(xs[0], xs[1]);
        return result;
    }

    private static void processCommand(String x, String y) {
        if (x.equalsIgnoreCase("N")) setConfig(x, Integer.parseInt(y));
        else
            // TODO sort this out
            if (x.equalsIgnoreCase("P")) //noinspection ResultOfMethodCallIgnored
                ForkJoinPool.getCommonPoolParallelism();
    }

    private static void setConfig(String x, int i) {
        configuration.put(x, i);
    }

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private static final Map<String, Integer> configuration = new HashMap<>();


}
