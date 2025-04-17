package com.Fidilite.FreeWatt.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Fidilite.FreeWatt.Entity.Achat;
import com.Fidilite.FreeWatt.Entity.Client;
import com.Fidilite.FreeWatt.dto.AchatDto;
import com.Fidilite.FreeWatt.repositories.AchatRepository;
import com.Fidilite.FreeWatt.repositories.ClientRepository;
import com.Fidilite.FreeWatt.type.PointTransactionType;

@Service
public class AchatService {

    @Autowired
    private AchatRepository achatRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ParameterService parameterService;

    @Autowired
    private PointTransactionService pointTransactionService; 

    public Achat addAchat(Long clientId, double montant) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client non trouvé"));

        double pointsGagnes = calculerPoints(montant);

        client.setTotalPoints(client.getTotalPoints() + (int) pointsGagnes);

        pointTransactionService.createPointTransaction(client, pointsGagnes, PointTransactionType.GAIN);

        Achat achat = new Achat();
        achat.setClient(client);
        achat.setMontant(montant);
        achat.setDate(LocalDateTime.now()); 
        return achatRepository.save(achat);
    }

    private double calculerPoints(double montant) {
        double cvrRate = parameterService.getConversionRate();
        return montant / cvrRate;
    }

    public List<AchatDto> getAllAchats() {
    return achatRepository.findAll()
        .stream()
        .map(achat -> new AchatDto(
            achat.getId(),
            achat.getMontant(),
            achat.getDate(),
            achat.getClient().getId()
        ))
        .collect(Collectors.toList());
}


    public List<AchatDto> getAchatsByClient(Long clientId) {
        return achatRepository.findByClientId(clientId).stream()
        .map(achat -> new AchatDto(
            achat.getId(),
            achat.getMontant(),
            achat.getDate(),
            achat.getClient().getId()
        ))
        .collect(Collectors.toList());
    }
}
