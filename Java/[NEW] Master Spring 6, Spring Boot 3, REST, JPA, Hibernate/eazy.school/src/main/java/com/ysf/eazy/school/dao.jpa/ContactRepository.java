package com.ysf.eazy.school.dao.jpa;

import com.ysf.eazy.school.model.jpa.ContactMessage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("contactRepositoryJPA")
public interface ContactRepository extends CrudRepository<ContactMessage, Integer> {

    List<ContactMessage> findByStatus(ContactMessage.MessageStatus status);
}
