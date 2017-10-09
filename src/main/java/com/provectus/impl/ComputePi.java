package com.provectus.impl;

import com.provectus.PiCalculationTask;
import com.provectus.Solution;
import com.server.interfaces.Compute;
import com.server.interfaces.Task;

import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.MathContext;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;
import java.util.concurrent.*;

public class ComputePi implements Solution{

    private LinkedList<Compute> computes;

    public ComputePi(LinkedList<Compute> computes) {
        this.computes = computes;
    }

    @Override
    public BigDecimal calculatePi(int digitsToCalculate) {

            // variables initialize block
            BigDecimal s1 = new BigDecimal(0);
            BigDecimal s2 = new BigDecimal(0);
            BigDecimal sub = s1.subtract(s2);
            BigDecimal accuracy = new BigDecimal(Math.pow(10, -digitsToCalculate));
            long offset = 0;
            long step = 100000;

            // 100 000 operations loop, break when accuracy achieved
            while (true) {

                ExecutorService executorService = Executors.newFixedThreadPool(3);
                List<Callable<BigDecimal>> callableList = generateCallableList(computes, offset, step);

                try {
                    List<Future<BigDecimal>> futures = executorService.invokeAll(callableList);
                    s1 = s1.add(sumParts(futures));

                    BigDecimal nthValue = new BigDecimal(4).divide(new BigDecimal(2 * (offset + step) - 1),  MathContext.DECIMAL128);
                    s2 = s1.subtract(nthValue);

                    offset = offset + step;
                    sub = s1.subtract(s2);
                    System.out.println("sub=" + sub);
                    if (sub.compareTo(accuracy) < 0) {
                        break;
                    }
                } catch (InterruptedException|ExecutionException e) {
                    System.err.println("Exception occurred");
                    e.printStackTrace();
                } finally {
                    executorService.shutdown();
                }
            }

            return s1.add(s2).divide(new BigDecimal(2)).setScale(digitsToCalculate, BigDecimal.ROUND_DOWN);

    }

    /**
     * Generate list of Callable interfaces which will call rest api ]
     *
     * @param offset
     * @param step
     * @return List<Callable<BigDecimal>>
     */
    private List<Callable<BigDecimal>> generateCallableList(LinkedList<Compute> computes, long offset, long step) {

        List<Callable<BigDecimal>> callableList = new ArrayList<>(3);

        for (int i=0; i<3; i++) {
            Map<String, Object> params = new HashMap<>();
            long startIndex = offset + (i * (step/3));

            long endIndex = startIndex + step/3;
            if (i == 2) {
                endIndex = endIndex + 1;
            }

            final int computeIndex = i;

            Task piTask = new PiCalculationTask(startIndex, endIndex);

            callableList.add(new Callable<BigDecimal>() {
                @Override
                public BigDecimal call() throws Exception {
                    return (BigDecimal)computes.get(computeIndex).computeTask(piTask);
                }
            });
        }

        return callableList;
    }

    /**
     * Summarize results returned by futures
     *
     * @param futures
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    private BigDecimal sumParts(List<Future<BigDecimal>> futures) throws ExecutionException, InterruptedException {

        BigDecimal res = new BigDecimal(0);

        for (Future<BigDecimal> future : futures) {
            res =  res.add(future.get());
        }

        return res;
    }

}
