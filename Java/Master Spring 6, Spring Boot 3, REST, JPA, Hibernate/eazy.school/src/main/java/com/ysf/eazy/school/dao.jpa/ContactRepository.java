package com.ysf.eazy.school.dao.jpa;

import com.ysf.eazy.school.model.jpa.ContactMessage;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("contactRepositoryJPA")
public interface ContactRepository extends JpaRepository<ContactMessage, Integer> {

    /*
     * For sorting the result set, the column names passed to the PageRequest object
     * should match the column names in the database.
     * For JPQL queries, the column names should match the field names in the entity.
     *
     * Need to handle ENUM value using spring expression language in the case of native query.
     */
//    @Query(value = "SELECT * FROM contact_msg c WHERE c.status = :#{#status.name()}", nativeQuery = true)

    @Query("SELECT c FROM ContactMessage c WHERE c.status = :status") // JPQL
    Page<List<ContactMessage>> findByStatus(ContactMessage.MessageStatus status, Pageable pageable);

    @Transactional
    @Modifying
    @Query("UPDATE ContactMessage c SET c.status = :status WHERE c.id = :id")
    int updateMessageStatus(ContactMessage.MessageStatus status, Integer id);
}
