package com.Fidilite.FreeWatt.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.Fidilite.FreeWatt.Entity.Achat;
import com.Fidilite.FreeWatt.Entity.Client;
import com.Fidilite.FreeWatt.dto.AchatDto;
import com.Fidilite.FreeWatt.repositories.AchatRepository;
import com.Fidilite.FreeWatt.repositories.ClientRepository;
import com.Fidilite.FreeWatt.type.PointTransactionType;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;

import org.slf4j.Logger;
/* import org.slf4j.LoggerFactory;
 */import org.springframework.beans.factory.annotation.Value;

@Service
public class AchatService {

    private static final Logger logger = LoggerFactory.getLogger(AchatService.class);

    @Value("${freewatt.email.sender}")
    private String senderEmail;

    @Autowired
    private AchatRepository achatRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ParameterService parameterService;

    @Autowired
    private PointTransactionService pointTransactionService;

    @Autowired
    private JavaMailSender javaMailSender;

    @Transactional
    public Achat addAchat(Long clientId, double montant) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client non trouvé"));

        double pointsGagnes = calculerPoints(montant);
        double newTotalPoints = client.getTotalPoints() + pointsGagnes;
        client.setTotalPoints((int) newTotalPoints); // Consider using double in Client entity

        pointTransactionService.createPointTransaction(client, pointsGagnes, PointTransactionType.GAIN);

        Achat achat = new Achat();
        achat.setClient(client);
        achat.setMontant(montant);
        achat.setDate(LocalDateTime.now());
        Achat savedAchat = achatRepository.save(achat);

        sendPurchaseEmail(client, montant, pointsGagnes, newTotalPoints);

        return savedAchat;
    }

    @Async
    public void sendPurchaseEmail(Client client, double montant, double pointsGagnes, double newTotalPoints) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage(); // Fixed variable name
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(senderEmail);
            helper.setTo(client.getEmail());
            helper.setSubject("Confirmation d'achat - FreeWatt");

            String content = buildEmailContent(client, montant, pointsGagnes, newTotalPoints);
            helper.setText(content, true);

            javaMailSender.send(message); // Fixed variable name
        } catch (MessagingException e) {
            logger.error("Échec de l'envoi de l'email au client {}: {}", client.getId(), e.getMessage());
        }
    }

    private String buildEmailContent(Client client, double montant, double pointsGagnes, double newTotalPoints) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><body style='font-family: Arial, sans-serif;'>");
        sb.append("<h2 style='color: #2c5f2d;'>Merci pour votre achat chez FreeWatt!</h2>");
        sb.append("<p>Détails de votre achat :</p>");
        sb.append("<ul>");
        sb.append("<li>Montant: ").append(String.format("%.2f DH", montant)).append("</li>");
        sb.append("<li>Points gagnés: ").append(pointsGagnes).append(" points</li>");
        sb.append("<li>Total points: ").append(newTotalPoints).append(" points</li>");
        sb.append("</ul>");

        int seuil = parameterService.getSeuilConversion();

        if (newTotalPoints >= 1000) {
            sb.append("<div style='background-color: #f8fff2; padding: 15px; border-radius: 5px; margin-top: 20px;'>");
            sb.append("<h3 style='color: #2c5f2d;'>🎉 Félicitations !</h3>");
            sb.append("<p>Vous avez atteint ").append(newTotalPoints).append(" points !</p>");
            sb.append(
                    "<p>Vous pouvez maintenant convertir vos points en argent réel et acheter ce que vous voulez dans notre boutique !</p>");
            sb.append(
                    "<a href='https://www.freewatt.com/conversion-points' style='background-color: #2c5f2d; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px;'>Convertir mes points</a>");
            sb.append("</div>");
        } else {
            sb.append("<p>Saviez-vous que lorsque vous atteindrez <strong>")
                    .append(seuil)
                    .append(" points</strong>, vous pourrez :</p>");
            sb.append("<ul>");
            sb.append("<li>Convertir vos points en argent réel</li>");
            sb.append("<li>Acheter des produits exclusifs</li>");
            sb.append("<li>Bénéficier d'offres spéciales</li>");
            sb.append("</ul>");
            sb.append("<p>Il vous manque seulement ").append(1000 - newTotalPoints)
                    .append(" points pour débloquer ces avantages !</p>");
        }

        sb.append("</body></html>");
        return sb.toString();
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
                        achat.getClient().getId()))
                .collect(Collectors.toList());
    }

    public List<AchatDto> getAchatsByClient(Long clientId) {
        return achatRepository.findByClientId(clientId).stream()
                .map(achat -> new AchatDto(
                        achat.getId(),
                        achat.getMontant(),
                        achat.getDate(),
                        achat.getClient().getId()))
                .collect(Collectors.toList());
    }
}
