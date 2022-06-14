package com.cqut.livechat.entity.message;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

/**
 * @author Augenstern
 * @date 2022/6/12
 */
@Getter
@Setter
@ToString(callSuper = true)
@Entity
@Table(name = "chat_files_message")
public class ChatFileMessage extends CommonMessage {
    @Column(name = "chat_file_name", nullable = false)
    private String originalFileName;
    @Column(name = "chat_file_size", nullable = false)
    private Long size;
    @Column(name = "chat_file_path", nullable = false)
    private String path;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        ChatFileMessage that = (ChatFileMessage) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
