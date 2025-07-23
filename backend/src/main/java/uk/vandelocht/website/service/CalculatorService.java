package uk.vandelocht.website.service;

import org.springframework.stereotype.Service;
import uk.vandelocht.website.dto.CalculationRequest;
import uk.vandelocht.website.exception.CalculationException;

@Service
public class CalculatorService {

    public Double calculate(CalculationRequest request) {
        Double num1 = request.getNum1();
        Double num2 = request.getNum2();
        String operation = request.getOperation();

        // Input validation
        if (num1 == null) {
            throw new CalculationException("First number cannot be null");
        }

        // Check for invalid numbers
        if (!isValidNumber(num1)) {
            throw new CalculationException("First number is invalid");
        }

        if (num2 != null && !isValidNumber(num2)) {
            throw new CalculationException("Second number is invalid");
        }

        return switch (operation) {
            case "add" -> {
                validateSecondNumber(num2);
                yield num1 + num2;
            }
            case "subtract" -> {
                validateSecondNumber(num2);
                yield num1 - num2;
            }
            case "multiply" -> {
                validateSecondNumber(num2);
                yield num1 * num2;
            }
            case "divide" -> {
                validateSecondNumber(num2);
                if (num2 == 0) {
                    throw new CalculationException("Division by zero is not allowed");
                }
                yield num1 / num2;
            }
            case "power" -> {
                validateSecondNumber(num2);
                // Limit power operations to prevent abuse
                if (Math.abs(num2) > 1000) {
                    throw new CalculationException("Exponent too large");
                }
                yield Math.pow(num1, num2);
            }
            case "sqrt" -> {
                if (num1 < 0) {
                    throw new CalculationException("Cannot calculate square root of negative number");
                }
                yield Math.sqrt(num1);
            }
            default -> throw new CalculationException("Invalid operation: " + operation);
        };
    }

    private void validateSecondNumber(Double num2) {
        if (num2 == null) {
            throw new CalculationException("Second number is required for this operation");
        }
    }

    private boolean isValidNumber(Double number) {
        return number != null && 
               !Double.isNaN(number) && 
               !Double.isInfinite(number) &&
               Math.abs(number) <= Double.MAX_VALUE;
    }
}