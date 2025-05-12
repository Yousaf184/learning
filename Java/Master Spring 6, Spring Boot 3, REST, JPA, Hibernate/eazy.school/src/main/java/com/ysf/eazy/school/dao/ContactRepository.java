package com.ysf.eazy.school.dao;

import com.ysf.eazy.school.model.ContactMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class ContactRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ContactRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int saveContactMessage(ContactMessage contactMessage) {
        String query = """
                INSERT INTO contact_msg (name, mobile_num, email, subject, message, status,
                created_at, created_by) VALUES (?,?,?,?,?,?,?,?)
                """;

        return this.jdbcTemplate.update(
                query, contactMessage.getName(), contactMessage.getMobileNum(), contactMessage.getEmail(),
                contactMessage.getSubject(), contactMessage.getMessage(), contactMessage.getStatus().name().toLowerCase(),
                contactMessage.getMetaInfo().getCreatedAt(), contactMessage.getMetaInfo().getCreatedBy()
        );
    }

    public List<ContactMessage> getContactMessages(String messageStatus) {
        String query = "SELECT * FROM CONTACT_MSG WHERE status = ?";

        PreparedStatementSetter preparedStatementSetter =
                ps -> ps.setString(1, messageStatus);

        RowMapper<ContactMessage> contactMsgRowMapper = (rs, rowNum) -> {
            ContactMessage contactMsg = new ContactMessage();

            contactMsg.setId(rs.getInt("contact_id"));
            contactMsg.setName(rs.getString("name"));
            contactMsg.setMobileNum(rs.getString("mobile_num"));
            contactMsg.setEmail(rs.getString("email"));
            contactMsg.setSubject(rs.getString("subject"));
            contactMsg.setMessage(rs.getString("message"));

            String msgStatus = rs.getString("status").toUpperCase();
            ContactMessage.MessageStatus status = ContactMessage.MessageStatus.valueOf(msgStatus);
            contactMsg.setStatus(status);

            LocalDateTime createdAt = rs.getTimestamp("created_at").toLocalDateTime();
            contactMsg.getMetaInfo().setCreatedAt(createdAt);
            contactMsg.getMetaInfo().setCreatedBy(rs.getString("created_by"));

            if (rs.getTimestamp("updated_at") != null) {
                LocalDateTime updatedAt = rs.getTimestamp("updated_at").toLocalDateTime();
                contactMsg.getMetaInfo().setCreatedAt(updatedAt);
            }

            if (rs.getString("updated_by") != null) {
                contactMsg.getMetaInfo().setUpdatedBy(rs.getString("updated_by"));
            }

            return contactMsg;
        };


        return this.jdbcTemplate.query(query, preparedStatementSetter, contactMsgRowMapper);
    }

    public int updateContactMessageStatus(Integer messageId, String newStatus, String updatedBy) {
        String query = """
                UPDATE CONTACT_MSG 
                SET status = ?, updated_at = ?, updated_by = ?
                WHERE contact_id = ?
                """;

        return this.jdbcTemplate.update(
                query,
                newStatus.toLowerCase(),
                LocalDateTime.now(),
                updatedBy,
                messageId
        );
    }
}
