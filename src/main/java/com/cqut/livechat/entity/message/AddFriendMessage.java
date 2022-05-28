package com.cqut.livechat.entity.message;

import com.cqut.livechat.constant.MessageStatus;
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
 * @date 2022/5/27
 */
@Entity
@Table(name = "chat_add_friend_message")
@Getter
@Setter
@ToString(callSuper = true)
@RequiredArgsConstructor
public class AddFriendMessage extends CommonMessage {

    @Column(name = "chat_status")
    private MessageStatus status = MessageStatus.PENDING;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        AddFriendMessage that = (AddFriendMessage) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
