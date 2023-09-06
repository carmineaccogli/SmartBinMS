package it.unisalento.pas.smartcitywastemanagement.smartbinms.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.domain.SmartBin;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.exceptions.SmartBinIsFullException;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.exceptions.SmartBinNotFoundException;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.rabbitMQMessages.SmartBinUpdateMessage;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.repositories.SmartBinRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import org.bson.types.Decimal128;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;
import java.util.Optional;
import java.util.Set;

@Service
public class RabbitMQ_DisposalListener {

    @Autowired
    ManageSmartBinsService manageSmartBinsService;

    @Autowired
    Validator validator;

    /** LISTENER DI COMUNICAZIONE DA DISPOSAL_MS A SMARTBIN_MS
     * (aggiornamento smartBin in arrivo di un conferimento)
     *
     * 1 Conversione del messaggio da stringa in SmartBinUpdateMessage
     * 2 Validazione dei campi presenti
     * 3 Gestione aggiornamento smartBin nell'apposito service
     *
     * @param message
     * @throws Exception
     */

    private static final Logger logger = LoggerFactory.getLogger(RabbitMQ_DisposalListener.class);

    @RabbitListener(queues = {"${rabbitmq.queue.smartBinUpdate}"})
    public void consumeMessage(@Payload  String message) throws Exception {

        // 1
        ObjectMapper objectMapper = new ObjectMapper();
        SmartBinUpdateMessage smartBinUpdateMessage = objectMapper.readValue(message, SmartBinUpdateMessage.class);

        logger.info("AMOUNT {}",smartBinUpdateMessage.getAmount());
        logger.info("ID {}", smartBinUpdateMessage.getSmartBinID());


        // 2
        String validationError = isMessageValid(smartBinUpdateMessage);
        if(validationError != null) {
            throw new AmqpRejectAndDontRequeueException(validationError);
        }

        // 3
        manageSmartBinsService.manageDisposalRequest(smartBinUpdateMessage.getSmartBinID(), smartBinUpdateMessage.getAmount());

    }


    private String isMessageValid(SmartBinUpdateMessage smartBinUpdateMessage) {

        Set<ConstraintViolation<SmartBinUpdateMessage>> violations = validator.validate(smartBinUpdateMessage);

        StringBuilder errorMessage = new StringBuilder("Errori di validazione: ");
        for (ConstraintViolation<SmartBinUpdateMessage> violation : violations) {
            errorMessage.append(violation.getMessage()).append(", ");
        }

        errorMessage.setLength(errorMessage.length() - 2);
        String errors = errorMessage.toString();

        if (!violations.isEmpty()) {
            return errors;
        }

        return null;
    }
}
