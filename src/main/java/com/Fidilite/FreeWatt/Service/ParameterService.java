package com.Fidilite.FreeWatt.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Fidilite.FreeWatt.Entity.Parameter;
import com.Fidilite.FreeWatt.repositories.ParameterRepository;
import com.Fidilite.FreeWatt.type.ParameterType;


@Service
public class ParameterService {

    @Autowired
    private ParameterRepository parameterRepository;

    public double getConversionRate() {
        return getParameterValue(ParameterType.CONVERSION_RATE, 10.0); // Mera lawla ettitha 10 par defaut
    }

    public int getSeuilConversion() {
        return getParameterValue(ParameterType.SEUIL_CONVERSION, 100); // hadi etitha 100
    }

    public void updateParameter(ParameterType type, String newValue) {
        Parameter param = parameterRepository.findByType(type)
            .orElse(new Parameter());
        param.setType(type);
        param.setValue(newValue);
        parameterRepository.save(param);
    }

    private double getParameterValue(ParameterType type, double defaultValue) {
        return parameterRepository.findByType(type)
                .map(p -> Double.parseDouble(p.getValue()))
                .orElse(defaultValue);
    }

    private int getParameterValue(ParameterType type, int defaultValue) {
        return parameterRepository.findByType(type)
                .map(p -> Integer.parseInt(p.getValue()))
                .orElse(defaultValue);
    }
}
