package com.akash.portfolio.service;

import com.akash.portfolio.entity.Contact;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /*
     * Send Contact Mail To Admin
     */
    public void sendContactMail(Contact contact) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo("akashsingh16423@gmail.com");

        message.setSubject(
                "New Portfolio Contact : " + contact.getSubject()
        );

        message.setText(

                "Name : " + contact.getName()

                        + "\n\nEmail : " + contact.getEmail()

                        + "\n\nMobile : " + contact.getMobile()

                        + "\n\nSubject : " + contact.getSubject()

                        + "\n\nMessage :\n\n"

                        + contact.getMessage()

        );

        mailSender.send(message);

    }

    /*
     * Auto Reply To Visitor
     */
    public void sendAutoReply(Contact contact) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(contact.getEmail());

        message.setSubject(
                "Thank you for contacting Akash Singh"
        );

        message.setText(

                "Hi " + contact.getName() + ","

                        + "\n\n"

                        + "Thank you for contacting me."

                        + "\n"

                        + "I have received your message."

                        + "\n"

                        + "I will get back to you as soon as possible."

                        + "\n\n"

                        + "Regards,"

                        + "\n"

                        + "Akash Singh"

                        + "\n"

                        + "Software Engineer"

        );

        mailSender.send(message);

    }

    /*
     * Send Custom Mail
     */
    public void sendMail(

            String to,

            String subject,

            String body

    ) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(to);

        message.setSubject(subject);

        message.setText(body);

        mailSender.send(message);

    }

    /*
     * Interview Invitation
     */
    public void sendInterviewMail(

            String email,

            String company,

            String interviewDate

    ) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(email);

        message.setSubject(
                "Interview Invitation"
        );

        message.setText(

                "Congratulations!"

                        + "\n\n"

                        + "You have been shortlisted."

                        + "\n"

                        + "Company : " + company

                        + "\n"

                        + "Interview Date : " + interviewDate

                        + "\n\n"

                        + "Best Wishes."

        );

        mailSender.send(message);

    }

    /*
     * Newsletter
     */
    public void sendNewsletter(

            String email,

            String title,

            String content

    ) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(email);

        message.setSubject(title);

        message.setText(content);

        mailSender.send(message);

    }

}