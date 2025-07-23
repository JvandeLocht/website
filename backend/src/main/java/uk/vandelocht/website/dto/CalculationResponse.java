package uk.vandelocht.website.dto;

public class CalculationResponse {
    private Double result;
    private String message;

    public CalculationResponse() {}

    public CalculationResponse(Double result) {
        this.result = result;
    }

    public CalculationResponse(String message) {
        this.message = message;
    }

    public Double getResult() {
        return result;
    }

    public void setResult(Double result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}