package com.provectus;

import com.provectus.impl.ComputePi;
import com.server.interfaces.Compute;

import java.math.BigDecimal;
import java.math.MathContext;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author nivanov, <a href="mailto:Nikita.Ivanov@returnonintelligence.com">Ivanov Nikita</a>
 * @since 08-Oct-17
 */
public class Main {
	public static void main(String[] args) {
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}
		try{

			LinkedList<Compute> computes = new LinkedList<>();
			computes.add(getRemoteObject(1099));
			computes.add(getRemoteObject(1100));
			computes.add(getRemoteObject(1101));

			Integer digitsToCalculate = Integer.parseInt(args[0]);
			ComputePi computePi = new ComputePi(computes);

			BigDecimal result = computePi.calculatePi(digitsToCalculate);

			printResult(result, digitsToCalculate);

		} catch (NotBoundException |RemoteException e) {
			System.err.println("Exception occurred");
			e.printStackTrace();
		}
	}

	/**
	 * Print result
	 *
	 * @param result
	 * @param digitsToCalculate
	 */
	private static void printResult(BigDecimal result, int digitsToCalculate){
		System.out.println("//////////////////////////////////////////////////////////////////////////");
		System.out.println("--------------------------------------------------------------------------");
		System.out.println("-----------------------------RESULT IS HERE: " + result + "--------------------");
		System.out.println("--------------------------------------------------------------------------");
		System.out.println("//////////////////////////////////////////////////////////////////////////");
	}

	private static Compute getRemoteObject(int port) throws RemoteException, NotBoundException {
		Registry registry = LocateRegistry.getRegistry(port);
		Compute compute = (Compute) registry.lookup("Compute");
		return (Compute) compute;
	}
}
