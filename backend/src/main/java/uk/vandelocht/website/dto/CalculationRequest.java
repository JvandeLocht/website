package uk.vandelocht.website.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class CalculationRequest {
    
    @NotNull(message = "Operation is required")
    @Pattern(regexp = "add|subtract|multiply|divide|power|sqrt", 
             message = "Invalid operation")
    private String operation;
    
    @NotNull(message = "First number is required")
    private Double num1;
    
    private Double num2;

    public CalculationRequest() {}

    public CalculationRequest(String operation, Double num1, Double num2) {
        this.operation = operation;
        this.num1 = num1;
        this.num2 = num2;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Double getNum1() {
        return num1;
    }

    public void setNum1(Double num1) {
        this.num1 = num1;
    }

    public Double getNum2() {
        return num2;
    }

    public void setNum2(Double num2) {
        this.num2 = num2;
    }
}