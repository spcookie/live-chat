package com.cqut.livechat.entity.auth;

import com.cqut.livechat.entity.BaseEntity;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

/**
 * @author Augenstern
 * @date 2022/5/22
 */
@Getter
@Setter
@ToString(callSuper = true)
@Entity
@Table(name = "chat_role")
public class Role extends BaseEntity {
    @Column(name = "chat_role_name", unique = true, nullable = false)
    private String roleName;
    @Column(name = "chat_role_describe")
    private String describe;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "chat_role_and_authority",
            joinColumns = {@JoinColumn(name = "role_id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_id")}
    )
    private Set<Authority> authorities;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Role role = (Role) o;
        return getId() != null && Objects.equals(getId(), role.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
