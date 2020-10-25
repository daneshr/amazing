package com.tradeshift.amazing.dto;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tradeshift.amazing.model.Item;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(value = Include.NON_NULL)
public class Tree {
	private String name;
	private int level;
	private List<Tree> children;

	public Tree(Item position) {
		this.name = position.getId().toString();
		this.level = position.getLevel();
	}

	public void addChild(Tree tree) {
		if (Objects.isNull(children)) {
			this.children = new LinkedList<Tree>();
		}
		children.add(tree);
	}
}
