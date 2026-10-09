package com.akash.portfolio.controller;

import com.akash.portfolio.dto.ContactDTO;
import com.akash.portfolio.entity.Contact;
import com.akash.portfolio.service.ContactService;
import com.akash.portfolio.service.EmailService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/contact")
public class ContactController {

    private static final Logger log = LoggerFactory.getLogger(ContactController.class);

    private final ContactService contactService;
    private final EmailService emailService;

    public ContactController(ContactService contactService,
                             EmailService emailService) {

        this.contactService = contactService;
        this.emailService = emailService;

    }

    /*
     * Contact Page
     */

    @GetMapping
    public String contactPage() {

        return "redirect:/#contact";

    }

    /*
     * Submit Contact Form
     */

    @PostMapping("/send")
    public String send(

            @Valid
            @ModelAttribute("contact")
            ContactDTO dto,

            BindingResult result,

            HttpServletRequest request,

            RedirectAttributes redirect

    ) {

        if (result.hasErrors()) {

            redirect.addFlashAttribute(
                    "org.springframework.validation.BindingResult.contact",
                    result);

            redirect.addFlashAttribute("contact", dto);

            return "redirect:/#contact";

        }

        Contact contact = new Contact();

        contact.setName(dto.getName());

        contact.setEmail(dto.getEmail());

        contact.setMobile(dto.getMobile());

        contact.setSubject(dto.getSubject());

        contact.setMessage(dto.getMessage());

        contact.setIpAddress(
                request.getRemoteAddr());

        contact.setUserAgent(
                request.getHeader("User-Agent"));

        contactService.save(contact);

        // The message is already saved, so a mail server problem
        // must not turn a successful submission into an error page.
        try {

            emailService.sendContactMail(contact);

            emailService.sendAutoReply(contact);

        } catch (Exception e) {

            log.warn("Contact saved but email not sent: {}", e.getMessage());

        }

        redirect.addFlashAttribute("contactSent", true);

        return "redirect:/#contact";

    }

    /*
     * Contact List
     */

    @GetMapping("/admin/list")
    public String list(Model model) {

        model.addAttribute(

                "contacts",

                contactService.getAllContacts()

        );

        return "admin/contact/list";

    }

    /*
     * View Message
     */

    @GetMapping("/admin/view/{id}")
    public String view(

            @PathVariable Long id,

            Model model

    ) {

        contactService.markAsRead(id);

        model.addAttribute(

                "contact",

                contactService.getContact(id)

        );

        return "admin/contact/view";

    }

    /*
     * Mark Unread
     */

    @GetMapping("/admin/unread/{id}")
    public String unread(

            @PathVariable Long id

    ) {

        contactService.markAsUnread(id);

        return "redirect:/contact/admin/list";

    }

    /*
     * Star Message
     */

    @GetMapping("/admin/star/{id}")
    public String star(

            @PathVariable Long id

    ) {

        contactService.starMessage(id);

        return "redirect:/contact/admin/list";

    }

    /*
     * Remove Star
     */

    @GetMapping("/admin/remove-star/{id}")
    public String removeStar(

            @PathVariable Long id

    ) {

        contactService.removeStar(id);

        return "redirect:/contact/admin/list";

    }

    /*
     * Mark Replied
     */

    @GetMapping("/admin/replied/{id}")
    public String replied(

            @PathVariable Long id

    ) {

        contactService.markAsReplied(id);

        return "redirect:/contact/admin/list";

    }

    /*
     * Delete Message
     */

    @GetMapping("/admin/delete/{id}")
    public String delete(

            @PathVariable Long id

    ) {

        contactService.deleteContact(id);

        return "redirect:/contact/admin/list";

    }

}