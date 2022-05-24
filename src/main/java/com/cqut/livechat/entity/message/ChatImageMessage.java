package com.cqut.livechat.entity.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

/**
 * @author Augenstern
 * @date 2022/5/24
 */
@Entity
@Table(name = "chat_image_message")
@Getter
@Setter
@ToString(callSuper = true)
@RequiredArgsConstructor
public class ChatImageMessage extends CommonMessage {

    @Column(name = "chat_image", columnDefinition = "blob", nullable = false)
    private byte[] image;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        ChatImageMessage that = (ChatImageMessage) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
