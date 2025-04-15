package com.Fidilite.FreeWatt.Controller;

import com.Fidilite.FreeWatt.Service.ParameterService;
import com.Fidilite.FreeWatt.type.ParameterType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/parameters")
public class ParameterController {

    @Autowired
    private ParameterService parameterService;

    // Récupérer le taux de conversion
    @GetMapping("/conversion-rate")
    public ResponseEntity<Double> getConversionRate() {
        double rate = parameterService.getConversionRate();
        return new ResponseEntity<>(rate, HttpStatus.OK);
    }

    // Récupérer le seuil de conversion
    @GetMapping("/seuil-conversion")
    public ResponseEntity<Integer> getSeuilConversion() {
        int seuil = parameterService.getSeuilConversion();
        return new ResponseEntity<>(seuil, HttpStatus.OK);
    }

    // Mettre à jour un paramètre
    @PutMapping("/{type}")
    public ResponseEntity<Void> updateParameter(@PathVariable String type, @RequestParam String newValue) {
        parameterService.updateParameter(ParameterType.valueOf(type), newValue);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
