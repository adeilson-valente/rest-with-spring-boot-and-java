package br.com.demo.services;

import br.com.demo.config.EmailConfig;
import br.com.demo.data.dto.request.EmailRequestDTO;
import br.com.demo.mail.EmailSender;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

@Service
public class EmailService {

    @Autowired
    private EmailSender emailSender;

    @Autowired
    private EmailConfig emailConfigs;

    public void sendSimpleEmail(EmailRequestDTO emailRequestDTO) {
        emailSender.
                to(emailRequestDTO.getTo())
                .withSubject(emailRequestDTO.getSubject())
                .withMessage(emailRequestDTO.getBody())
                .send(emailConfigs);
    }

    public void sendEmailWithAttachment(String emailRequestJson, MultipartFile attachment) {
        File tempFile = null;

        try {
            EmailRequestDTO emailRequestDTO = new ObjectMapper().readValue(emailRequestJson, EmailRequestDTO.class);
            tempFile = File.createTempFile("attachment", attachment.getOriginalFilename());

            attachment.transferTo(tempFile);

            emailSender.
                    to(emailRequestDTO.getTo())
                    .withSubject(emailRequestDTO.getSubject())
                    .withMessage(emailRequestDTO.getBody())
                    .atthach(tempFile.getAbsolutePath())
                    .send(emailConfigs);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error parsing email reques JSON", e);
        } catch (IOException e) {
            throw new RuntimeException("Error processing the attachment", e);
        }
        finally {
            if(tempFile != null && tempFile.exists()){
                tempFile.delete();
            }
        }
    }
}
