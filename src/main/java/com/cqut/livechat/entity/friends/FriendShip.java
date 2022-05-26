package com.cqut.livechat.entity.friends;

import com.cqut.livechat.entity.BaseEntity;
import com.cqut.livechat.entity.auth.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Objects;

/**
 * @author Augenstern
 * @date 2022/5/25
 */
@Entity
@Table(name = "chat_friend_ship")
@Getter
@Setter
@ToString(callSuper = true)
@RequiredArgsConstructor
public class FriendShip extends BaseEntity {

    @ManyToOne(optional = false)
    @JoinColumn(name = "chat_user_id")
    private User user;
    @ManyToOne(optional = false)
    @JoinColumn(name = "chat_friend_id")
    private User friend;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        FriendShip friend = (FriendShip) o;
        return getId() != null && Objects.equals(getId(), friend.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}