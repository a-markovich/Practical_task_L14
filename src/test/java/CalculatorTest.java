import Lesson_14_junit_5.Calculator;
import Lesson_14_junit_5.FactorialException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CalculatorTest {
    @ParameterizedTest(name = "{index}: число {0} меньше 0")
    @ValueSource(ints = {-1, -10, -100})
    public void getFactorialForNegativeNumTest(int num) {
        Exception exception = assertThrows(FactorialException.class, () -> {
            Calculator.getFactorial(num);
        });
        assertEquals("Число меньше 0", exception.getMessage());
    }

    @ParameterizedTest(name = "{index}:   {0}! = {1}")
    @CsvSource({
            "0, 1",
            "1, 1",
            "2, 2",
            "3, 6",
            "5, 120",
            "10, 3628800",
            "15, 1307674368000",
            "30, 265252859812191058636308480000000",
            "40, 815915283247897734345611269596115894272000000000"
    })
    public void getFactorialForPositiveNumTest(int actual, String expected) throws FactorialException {
        BigInteger actualResult = Calculator.getFactorial(actual);
        BigInteger expectedResult = new BigInteger(expected);
        assertEquals(0, actualResult.compareTo(expectedResult));
    }
}
