package com.provectus;

import com.server.interfaces.Task;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;

/**
 * @author nivanov, <a href="mailto:Nikita.Ivanov@returnonintelligence.com">Ivanov Nikita</a>
 * @since 01-Oct-17
 */
public class PiCalculationTask implements Task<BigDecimal>, Serializable {

	private static final long serialVersionUID = 223L;

	private long startIndex;
	private long endIndex;

	public PiCalculationTask(long startIndex, long endIndex) {
		this.startIndex = startIndex;
		this.endIndex = endIndex;
	}

	@Override
	public BigDecimal execute() {
		return computePi(startIndex, endIndex);
	}

	private BigDecimal computePi(long startIndex, long endIndex) {
		BigDecimal pi = new BigDecimal(0);
		System.out.println("Thread name:" + Thread.currentThread().getName() + " STARTING INDEX:" + startIndex);
		System.out.println("Thread name:" + Thread.currentThread().getName() + " END INDEX:" + endIndex);

		if (startIndex == 0) {
			startIndex++;
		}

		for(long i = startIndex; i < endIndex; i++) {
			BigDecimal iItem = new BigDecimal(4).divide(new BigDecimal(2 * i - 1),  MathContext.DECIMAL128);
			if (i % 2 == 0) {
				pi = pi.subtract(iItem);
			} else {
				pi = pi.add(iItem);
			}
		}

		System.out.println("pi=" + pi);

		return pi;
	}
}
