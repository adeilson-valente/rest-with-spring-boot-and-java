package br.com.demo.api.v1.controller;

import br.com.demo.api.v1.docs.EmailControllerDocs;
import br.com.demo.data.dto.request.EmailRequestDTO;
import br.com.demo.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/email")
public class EmailController implements EmailControllerDocs {

    @Autowired
    private EmailService emailService;

    @PostMapping()
    @Override
    public ResponseEntity<String> sendEmail(@RequestBody EmailRequestDTO emailRequestDTO) {
        emailService.sendSimpleEmail(emailRequestDTO);
        return new ResponseEntity<>("e-Mail sent with success!", HttpStatus.OK);
    }

    @PostMapping(value = "/withattachment", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Override
    public ResponseEntity<String> sendEmailWithAttachment(@RequestParam("emailRequest") String emailRequest, @RequestParam("attachment") MultipartFile attachment) {
        emailService.sendEmailWithAttachment(emailRequest, attachment);
        return new ResponseEntity<>("e-Mail sent with attachment sent successfuly!", HttpStatus.OK);
    }
}
