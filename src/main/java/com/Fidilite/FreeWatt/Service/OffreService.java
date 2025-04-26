package com.Fidilite.FreeWatt.Service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.Fidilite.FreeWatt.Entity.Client;
import com.Fidilite.FreeWatt.Entity.Offre;
import com.Fidilite.FreeWatt.repositories.ClientRepository;
import com.Fidilite.FreeWatt.repositories.OffreRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class OffreService {

    private static final Logger logger = LoggerFactory.getLogger(AchatService.class);

    @Autowired
    private OffreRepository offreRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private ClientRepository clientRepository;

    @Value("${spring.mail.username}")
    private String senderEmail;

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

    @Async
    public void envoyerOffreAClients(Offre offre) {
        List<Client> clients = clientRepository.findAll();

        clients.forEach(client -> {
            try {
                MimeMessage message = javaMailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

                helper.setFrom(senderEmail);
                helper.setTo(client.getEmail());
                helper.setSubject("Nouvelle offre disponible - " + offre.getTitre());

                String content = buildOfferEmailContent(client, offre);
                helper.setText(content, true);

                javaMailSender.send(message);
            } catch (MessagingException e) {
                logger.error("Échec d'envoi d'email pour l'offre {} au client {}", offre.getId(), client.getId(), e);
            }
        });
    }

    private String buildOfferEmailContent(Client client, Offre offre) {
        return "<html><body style='font-family: Arial, sans-serif;'>"
                + "<h2 style='color: #2c5f2d;'>Nouvelle offre disponible chez FreeWatt!</h2>"
                + "<div style='border: 1px solid #e0e0e0; padding: 20px; border-radius: 10px;'>"
                + "<h3>" + offre.getTitre() + "</h3>"
                + "<p>" + offre.getDescription() + "</p>"
                + "<div style='background-color: #f8f9fa; padding: 15px; border-radius: 5px; margin-top: 15px;'>"
                + "<p><strong>Points nécessaires :</strong> " + offre.getPointsNecessaires() + " points</p>"
                + "</div>"
                + "<p style='margin-top: 20px;'>Votre solde actuel : " + client.getTotalPoints() + " points</p>"
                + "</div></body></html>";
    }

}
