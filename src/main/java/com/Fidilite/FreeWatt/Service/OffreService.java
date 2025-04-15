package com.Fidilite.FreeWatt.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Fidilite.FreeWatt.Entity.Offre;
import com.Fidilite.FreeWatt.repositories.OffreRepository;

@Service
public class OffreService {

    @Autowired
    private OffreRepository offreRepository;

    public Offre addOffre(String titre, int pointsNecessaires, String description) {
        Offre offre = new Offre(titre, description, pointsNecessaires);
        return offreRepository.save(offre);
    }

    public Offre updateOffre(Long offreId, String titre, String description, int pointsNecessaires) {
        Offre offre = offreRepository.findById(offreId)
            .orElseThrow(() -> new RuntimeException("Offre non trouvée"));

        offre.setTitre(titre);
        offre.setDescription(description);
        offre.setPointsNecessaires(pointsNecessaires);
        return offreRepository.save(offre);
    }

    public void deleteOffre(Long offreId) {
        offreRepository.deleteById(offreId);
    }

    public List<Offre> getAllOffres() {
        return offreRepository.findAll();
    }

    public Offre getOffreById(Long offreId) {
        return offreRepository.findById(offreId)
            .orElseThrow(() -> new RuntimeException("Offre non trouvée"));
    }
}
