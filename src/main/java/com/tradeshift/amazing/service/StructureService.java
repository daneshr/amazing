package com.tradeshift.amazing.service;

import java.util.List;

import com.tradeshift.amazing.dto.Tree;
import com.tradeshift.amazing.model.Item;

public interface StructureService {
	Item getSingleItem(long id);

	List<Item> getAllDescendantItems(long id);

	void changeParent(long id, long newParentId);

	void insertNewStructure(Item root);

	void addNewSubStructureToSpecificLocation(Item position, long locationId);

	Tree getSubStructure(long id);

	Tree getWholeStructure();

}
