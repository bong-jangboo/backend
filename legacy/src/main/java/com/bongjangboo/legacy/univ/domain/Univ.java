package com.bongjangboo.legacy.univ.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="univ")
public class Univ {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Getter
	private Long id;

	@Getter
	@Column(name="name")
	private String name;

	@Getter
	@Enumerated(EnumType.STRING)
	@Column(name="org_type")
	private OrgType orgType;

	@Getter
	@ManyToOne
	@JoinColumn(name = "parent_id")
	private Univ parent;

	@OneToMany(mappedBy = "parent")
	private List<Univ> children = new ArrayList<>();

	@Builder
	public Univ(String name, String orgType, Univ parent) {
		this.name = name;
		this.orgType = OrgType.valueOf(orgType);
		this.parent = parent;
	}

}
