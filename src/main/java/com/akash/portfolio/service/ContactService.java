package com.akash.portfolio.service;

import com.akash.portfolio.entity.Contact;
import com.akash.portfolio.repository.ContactRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactService {

    private final ContactRepository repository;

    public ContactService(ContactRepository repository) {
        this.repository = repository;
    }

    /*
     * Save Contact
     */
    public Contact save(Contact contact) {

        return repository.save(contact);

    }

    /*
     * Get Contact By ID
     */
    public Contact getContact(Long id) {

        return repository.findById(id)

                .orElseThrow(() ->
                        new RuntimeException("Contact not found."));

    }

    /*
     * Get All Contacts
     */
    public List<Contact> getAllContacts() {

        return repository.findByDeletedFalseOrderByCreatedAtDesc();

    }

    /*
     * Latest Contacts
     */
    public List<Contact> getLatestContacts() {

        return repository.findByDeletedFalseOrderByCreatedAtDesc();

    }

    /*
     * Unread Messages
     */
    public List<Contact> getUnreadMessages() {

        return repository.findByReadStatusFalseOrderByCreatedAtDesc();

    }

    /*
     * Starred Messages
     */
    public List<Contact> getStarredMessages() {

        return repository.findByStarredTrueOrderByCreatedAtDesc();

    }

    /*
     * Mark As Read
     */
    public void markAsRead(Long id) {

        Contact contact = getContact(id);

        contact.setReadStatus(true);

        repository.save(contact);

    }

    /*
     * Mark As Unread
     */
    public void markAsUnread(Long id) {

        Contact contact = getContact(id);

        contact.setReadStatus(false);

        repository.save(contact);

    }

    /*
     * Mark As Replied
     */
    public void markAsReplied(Long id) {

        Contact contact = getContact(id);

        contact.setReplied(true);

        repository.save(contact);

    }

    /*
     * Star Message
     */
    public void starMessage(Long id) {

        Contact contact = getContact(id);

        contact.setStarred(true);

        repository.save(contact);

    }

    /*
     * Remove Star
     */
    public void removeStar(Long id) {

        Contact contact = getContact(id);

        contact.setStarred(false);

        repository.save(contact);

    }

    /*
     * Soft Delete
     */
    public void deleteContact(Long id) {

        Contact contact = getContact(id);

        contact.setDeleted(true);

        repository.save(contact);

    }

    /*
     * Total Messages
     */
    public long totalMessages() {

        return repository.count();

    }

    /*
     * Unread Count
     */
    public long unreadMessagesCount() {

        return repository.countByReadStatusFalse();

    }

}