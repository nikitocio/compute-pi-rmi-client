package com.provectus;

import java.math.BigDecimal;
import java.util.concurrent.Future;

/**
 * @author nivanov, <a href="mailto:Nikita.Ivanov@returnonintelligence.com">Ivanov Nikita</a>
 * @since 08-Oct-17
 */
public interface Solution {
	BigDecimal calculatePi(int numberOfCorrectDigits);
}
