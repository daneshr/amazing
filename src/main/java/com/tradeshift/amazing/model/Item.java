package com.tradeshift.amazing.model;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Item implements Comparable<Item> {

	@Id
	private Long id;

	private int leftLimit;

	private int rightLimit;

	private int level;

	@ManyToOne
	@JoinColumn(name = "parent_id")
	private Item parent;

	@Override
	public int compareTo(Item that) {
		if (this.level == that.level) {
			return this.leftLimit - that.leftLimit;
		}
		return this.level - that.level;
	}

	@Transient
	private List<Item> children = new LinkedList<Item>();

}
