package com.cqut.livechat.entity.user;

import com.cqut.livechat.entity.BaseEntity;
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
 * @date 2022/5/25
 */
@Table(name = "chat_account")
@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Account extends BaseEntity {

    @Column(name = "chat_account_username", length = 11, nullable = false)
    private String username;
    @Column(name = "chat_account_name", length = 4, nullable = false)
    private String name;
    @Column(name = "chat_account_phone", length = 11)
    private String phone;
    @Column(name = "chat_account_age", length = 3)
    private Integer age;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Account account = (Account) o;
        return getId() != null && Objects.equals(getId(), account.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
