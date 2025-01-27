package com.ysf.eazy.school.dao.jpa;

import com.ysf.eazy.school.model.jpa.ContactMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("contactRepositoryJPA")
public interface ContactRepository extends JpaRepository<ContactMessage, Integer> {

    Page<List<ContactMessage>> findByStatus(ContactMessage.MessageStatus status, Pageable pageable);
}
