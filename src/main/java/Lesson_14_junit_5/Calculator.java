package Lesson_14_junit_5;

import java.math.BigInteger;

public class Calculator {
    public static BigInteger getFactorial(int num) throws FactorialException {
        BigInteger result = BigInteger.ONE;
        if (num < 0) {
            throw new FactorialException("Число меньше 0");
        } else if (num == 0) {
            return result;
        }
        for (int i = 1; i <= num; i++) {
            result = result.multiply(BigInteger.valueOf(i));
        }
        return result;
    }
}
