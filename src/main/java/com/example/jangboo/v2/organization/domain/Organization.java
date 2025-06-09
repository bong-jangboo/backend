package com.example.jangboo.v2.organization.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString(exclude = {"parent", "children"})
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "organization_id")
    private Long id;

    @Column(name="name")
    private String name;


    @Enumerated(EnumType.STRING)
    @Column(name = "org_type")
    private OrgType orgType;

    @Enumerated(EnumType.STRING)
    @Column(name = "org_status")
    private OrgStatus orgStatus;

    // 상위 조직
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id") // DB에 저장될 외래 키 컬럼명
    private Organization parent;

    // 하위 조직 목록
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Organization> children = new ArrayList<>();

    @Builder
    public Organization(String name, OrgType orgType, OrgStatus orgStatus, Organization parent) {
        this.name = name;
        this.orgType = orgType;
        this.orgStatus = orgStatus;
        this.parent = parent;
    }

    public void addChild(Organization child) {
        children.add(child);
        child.parent = this;
    }


}
