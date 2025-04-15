package com.Fidilite.FreeWatt.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Fidilite.FreeWatt.Service.ParameterService;
import com.Fidilite.FreeWatt.type.ParameterType;


@RestController
@RequestMapping("/api/admin/config")
public class AdminConfigController {

    @Autowired
    private ParameterService parameterService;

    @GetMapping("/conversion-rate")
    public double getConversionRate() {
        return parameterService.getConversionRate();
    }

    @GetMapping("/seuil-conversion")
    public int getSeuilConversion() {
        return parameterService.getSeuilConversion();
    }

    @PutMapping
    public ResponseEntity<Void> updateConfig(@RequestParam ParameterType type, @RequestParam String value) {
        parameterService.updateParameter(type, value);
        return ResponseEntity.ok().build();
    }
}

