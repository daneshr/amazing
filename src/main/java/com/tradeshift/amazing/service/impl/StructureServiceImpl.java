package com.tradeshift.amazing.service.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.server.ResponseStatusException;

import com.tradeshift.amazing.dto.Tree;
import com.tradeshift.amazing.model.Item;
import com.tradeshift.amazing.repository.CompanyStructureRepository;
import com.tradeshift.amazing.service.StructureService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class StructureServiceImpl implements StructureService {

	private final CompanyStructureRepository companyStructureRepository;

	@Override
	public Item getSingleItem(long id) {
		Item item = companyStructureRepository.findById(id).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("id: %d not found", id)));
		log.info(String.format("item with id:%d is retrived successfully", item.getId()));
		return item;
	}

	@Override
	public Tree getWholeStructure() {
		Item root = companyStructureRepository.findByParentIsNull()
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "structure is empty"));
		log.info("root item is retrieved.");
		Tree tree = getStructure(root);
		log.info("tree created.");
		return tree;
	}

	@Override
	public List<Item> getAllDescendantItems(long id) {
		Item cur = companyStructureRepository.findById(id).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("id: %d not found", id)));
		return companyStructureRepository.findByLeftLimitGreaterThanAndRightLimitLessThan(cur.getLeftLimit(),
				cur.getRightLimit());
	}

	@Override
	public Tree getSubStructure(long id) {
		Item item = companyStructureRepository.findById(id).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("id: %d not found", id)));
		return getStructure(item);
	}

	private Tree getStructure(Item item) {
		List<Item> items = companyStructureRepository
				.findByLeftLimitGreaterThanAndRightLimitLessThan(item.getLeftLimit(), item.getRightLimit());
		items.add(item);
		return transformItemsToTree(items);
	}

	@Override
	public void changeParent(long id, long newParentId) {
		Assert.isTrue(!(id == newParentId), String.format("item id and parent id should be different id:%d", id));
		Item item = companyStructureRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
						String.format("item with id: %d not found", id)));

		Item newParent = companyStructureRepository.findById(newParentId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
						String.format("new parent with id: %d not found", newParentId)));
		List<Item> items = companyStructureRepository
				.findByLeftLimitGreaterThanAndRightLimitLessThan(item.getLeftLimit(), item.getRightLimit());

		if (items.stream().filter((child) -> newParent.getId().compareTo(child.getId()) == 0).findAny().isPresent()) {
			throw new ResponseStatusException(HttpStatus.CONFLICT,
					String.format("item with id: %d is ancestor of item with id:%d", id, newParentId));
		}
		companyStructureRepository.moveSubTree(item, newParent);
		log.info(String.format("item with id:%d was moved to parent with id:%d", id, newParentId));
	}

	@Override
	public void insertNewStructure(Item root) {
		validateInput(root);
		transformToNestedSetFormat(root);
		companyStructureRepository.deleteAll();
		saveTree(root);
		log.info(String.format("new structure with root id:%d was created", root.getId()));
	}

	private void validateInput(Item root) {
		Set<Long> items = new HashSet<Long>();
		validateInput(root, items);
	}

	private void validateInput(Item item, Set<Long> items) {
		if (Objects.isNull(item.getId()) || item.getId() == 0) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "empty or 0 id is not allowed");
		}
		if (items.contains(item.getId())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					String.format("Duplicated ids are not allowed, id: %d ", item.getId()));
		}
		items.add(item.getId());
		if (Objects.nonNull(item.getChildren())) {
			for (Item elem : item.getChildren()) {
				validateInput(elem, items);
			}
		}
	}

	@Override
	public void addNewSubStructureToSpecificLocation(Item root, long locationId) {
		addSubStructure(root, locationId);

	}

	private Tree transformItemsToTree(List<Item> items) {
		Collections.sort(items);
		Tree root = null;
		Map<Long, Tree> nodesMemo = new HashMap<Long, Tree>();
		for (Item item : items) {
			Tree tree = new Tree(item);
			nodesMemo.put(item.getId(), tree);
			if (Objects.isNull(root)) {
				root = tree;
			} else {
				nodesMemo.get(item.getParent().getId()).addChild(tree);
			}
		}
		return root;
	}

	public void addSubStructure(Item root, long locationId) {
		Item parent = companyStructureRepository.getOne(locationId);
		int insertLocation = parent.getLeftLimit() + 1;
		root.setParent(parent);
		transformToNestedSetFormat(root, insertLocation, parent.getLevel() + 1);
		int innerSetSize = root.getRightLimit() - root.getLeftLimit() + 1;
		companyStructureRepository.moveLeftsToCreateNewSpace(insertLocation, innerSetSize);
		companyStructureRepository.moveRightsToCreateNewSpace(insertLocation, innerSetSize);

		saveTree(root);
	}

	@Transactional
	public void saveTree(Item node) {
		companyStructureRepository.save(node);
		for (Item child : node.getChildren()) {
			saveTree(child);
		}
	}

	private Item transformToNestedSetFormat(Item root) {
		transformToNestedSetFormat(root, 1, 0);
		return root;
	}

	private int transformToNestedSetFormat(Item node, int counter, int level) {
		node.setLeftLimit(counter);
		node.setLevel(level);
		for (Item child : node.getChildren()) {
			child.setParent(node);
			counter = transformToNestedSetFormat(child, counter + 1, level + 1);
		}
		node.setRightLimit(++counter);
		return counter;
	}

}
