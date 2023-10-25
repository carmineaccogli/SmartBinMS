package it.unisalento.pas.smartcitywastemanagement.smartbinms.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.unisalento.pas.smartcitywastemanagement.smartbinms.rabbitMQMessages.SmartBinUpdateMessage;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = {RabbitMQ_DisposalListener.class})
public class RabbitMQ_DisposalListenerTest {

    @InjectMocks
    private RabbitMQ_DisposalListener rabbitMQDisposalListener;

    @MockBean
    private ManageSmartBinsService manageSmartBinsService;

    @MockBean
    private Validator validator;

    @MockBean
    private ObjectMapper objectMapper;



    @Test
    public void testConsumeMessage() throws Exception {

        SmartBinUpdateMessage validMessage = new SmartBinUpdateMessage();
        validMessage.setSmartBinID("TestID");
        validMessage.setAmount(new BigDecimal(10));

        String jsonString = "{\"smartBinID\":\""+validMessage.getSmartBinID()+"\",\"amount\":"+validMessage.getAmount()+"}";

        Mockito.when(objectMapper.readValue(jsonString, SmartBinUpdateMessage.class)).thenReturn(validMessage);

        rabbitMQDisposalListener.consumeMessage(jsonString);

        Mockito.verify(manageSmartBinsService).manageDisposalRequest(validMessage.getSmartBinID(), validMessage.getAmount());
    }

}
