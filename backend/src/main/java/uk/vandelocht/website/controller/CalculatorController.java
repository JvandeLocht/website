package uk.vandelocht.website.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import uk.vandelocht.website.dto.CalculationRequest;
import uk.vandelocht.website.dto.CalculationResponse;
import uk.vandelocht.website.exception.CalculationException;
import uk.vandelocht.website.service.CalculatorService;

@RestController
@RequestMapping("/api")
public class CalculatorController {

    private final CalculatorService calculatorService;

    @Autowired
    public CalculatorController(CalculatorService calculatorService) {
        this.calculatorService = calculatorService;
    }

    @PostMapping("/calculate")
    public ResponseEntity<CalculationResponse> calculate(
            @Valid @RequestBody CalculationRequest request,
            BindingResult bindingResult) {
        
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .findFirst()
                    .orElse("Invalid input");
            
            return ResponseEntity.badRequest()
                    .body(new CalculationResponse(errorMessage));
        }

        try {
            Double result = calculatorService.calculate(request);
            return ResponseEntity.ok(new CalculationResponse(result));
        } catch (CalculationException e) {
            return ResponseEntity.badRequest()
                    .body(new CalculationResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CalculationResponse("Internal server error"));
        }
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("Calculator service is running");
    }
}