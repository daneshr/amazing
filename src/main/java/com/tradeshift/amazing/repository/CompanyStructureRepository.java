package com.tradeshift.amazing.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tradeshift.amazing.model.Item;

@Repository
public interface CompanyStructureRepository extends JpaRepository<Item, Long> {

	/**
	 * Moving subtree in Nested Sets Change an Item parent to new one. An Item can
	 * not moved to itself. This method do these operations: 1- Creates a space for
	 * the item and it's children at the parent first child location 2- Moves the
	 * item structure to new place 3- Compresses wasted spaces.
	 * 
	 * @param item       to move
	 * @param newParent, new location
	 */
	public default void moveSubTree(Item item, Item newParent) {

		int innerSetSize = item.getRightLimit() - item.getLeftLimit() + 1;
		int insertPosition = newParent.getLeftLimit() + 1;
		int levelGap = newParent.getLevel() - item.getLevel() + 1;
		int distance = insertPosition - item.getLeftLimit();

		int itemLeftMemo = item.getLeftLimit();
		int itemRightMemo = item.getRightLimit();

		// create space for sub tree
		moveLeftsToCreateNewSpace(insertPosition, innerSetSize);
		moveRightsToCreateNewSpace(insertPosition, innerSetSize);

		// backwards movement
		if (distance < 0) {
			distance -= innerSetSize;
			itemLeftMemo += innerSetSize;
		}
		// move
		moveSubTreeToNewSpace(itemLeftMemo, itemLeftMemo + innerSetSize, distance, levelGap);
		// update parent id
		updateParentPosition(item.getId(), newParent.getId());

		// Compress space
		removeOldVacatedLeftSpace(itemRightMemo, innerSetSize);
		removeOldVacatedRightSpace(itemRightMemo, innerSetSize);
	}

	List<Item> findByLeftLimitGreaterThanAndRightLimitLessThan(int left, int right);

	Optional<Item> findByParentIsNull();

	@Transactional
	@Modifying
	@Query("UPDATE Item i SET i.leftLimit = i.leftLimit + :innerSetSize  WHERE i.leftLimit >= :insertPosition")
	void moveLeftsToCreateNewSpace(@Param("insertPosition") int insertPosition,
			@Param("innerSetSize") int innerSetSize);

	@Transactional
	@Modifying
	@Query("UPDATE Item i SET i.rightLimit = i.rightLimit + :innerSetSize  WHERE i.rightLimit >= :insertPosition")
	void moveRightsToCreateNewSpace(@Param("insertPosition") int insertPosition,
			@Param("innerSetSize") int innerSetSize);

	@Transactional
	@Modifying
	@Query("UPDATE Item i "
			+ "SET i.leftLimit = i.leftLimit + :distance, i.rightLimit = i.rightLimit + :distance, i.level = i.level + :levelGap "
			+ "WHERE i.leftLimit >= :fromLimit AND i.rightLimit < :toLimit ")
	void moveSubTreeToNewSpace(@Param("fromLimit") int fromLimit, @Param("toLimit") int toLimit,
			@Param("distance") int distance, @Param("levelGap") int levelGap);

	@Transactional
	@Modifying
	@Query("UPDATE Item i SET i.parent.id = :newParentId  WHERE i.id = :itemId")
	void updateParentPosition(@Param("itemId") long itemId, @Param("newParentId") Long newParentId);

	@Transactional
	@Modifying
	@Query("UPDATE Item i SET i.leftLimit = i.leftLimit - :innerSetSize  WHERE i.leftLimit > :itemRightMemo")
	void removeOldVacatedLeftSpace(@Param("itemRightMemo") int itemRightMemo, @Param("innerSetSize") int innerSetSize);

	@Transactional
	@Modifying
	@Query("UPDATE Item i SET i.rightLimit = i.rightLimit - :innerSetSize  WHERE i.rightLimit > :itemRightMemo ")
	void removeOldVacatedRightSpace(@Param("itemRightMemo") int itemRightMemo, @Param("innerSetSize") int innerSetSize);

}
