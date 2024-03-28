//package com.jabar.app.fitnesscenter.feature.user.entity.model;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//import java.io.Serializable;
//import java.util.Objects;
//import java.util.UUID;
//
//@Getter
//@Setter
//@Entity
//@NoArgsConstructor
//@AllArgsConstructor
//public class AppRole implements Serializable {
//    @Id
//    private String roleId;
//
//    @Enumerated(EnumType.STRING)
//    @Column
//    private ERole roleName;
//
//    @Column(length = 100)
//    private String roleDescription;
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(getRoleName());
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if(this == o) return true;
//        if(!(o instanceof AppRole role) || !super.equals(o)) return false;
//        return Objects.equals(getRoleName(), role.roleName);
//    }
//
//    @PrePersist
//    private void onGenerateId() {
//        if(roleId == null || roleId.isEmpty()) {
//            setRoleId(UUID.randomUUID().toString());
//        }
//    }
//}
