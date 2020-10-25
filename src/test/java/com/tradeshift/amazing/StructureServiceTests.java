package com.tradeshift.amazing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tradeshift.amazing.model.Item;
import com.tradeshift.amazing.repository.CompanyStructureRepository;
import com.tradeshift.amazing.service.impl.StructureServiceImpl;

@ExtendWith(MockitoExtension.class)
public class StructureServiceTests {

	@Mock
	CompanyStructureRepository structureRepository;

	@InjectMocks
	StructureServiceImpl structureService;

	@InjectMocks
	private ObjectMapper objectMapper;

	@Test
	void shouldThrowException_whenReceiveSameItemIdAndParentIdAtChangeParent() throws IOException {
		Assertions.assertThrows(RuntimeException.class, () -> structureService.changeParent(5, 5));
	}

	@Test
	void shouldThrowException_whenReceiveNotFoundIdAtChangeParent() throws IOException {
		when(structureRepository.findById(100L)).thenReturn(Optional.empty());
		Assertions.assertThrows(RuntimeException.class, () -> structureService.changeParent(100, 1));
	}

	@Test
	void shouldThrowException_whenReceiveNotFoundParentIdAtChangeParent() throws IOException {

		when(structureRepository.findById(1L)).thenReturn(Optional.of(new Item()));
		when(structureRepository.findById(100L)).thenReturn(Optional.empty());
		Assertions.assertThrows(RuntimeException.class, () -> structureService.changeParent(1, 100));
	}

	@Test
	void shouldThrowException_whenReceiveDescendantParentIdAtChangeParent() throws IOException {
		Item toMove = new Item();
		toMove.setId(1l);

		Item parent = new Item();
		toMove.setId(2l);
		List<Item> toMoveDescendants = new LinkedList<Item>();
		toMoveDescendants.add(parent);

		when(structureRepository.findById(1L)).thenReturn(Optional.of(toMove));
		when(structureRepository.findById(2L)).thenReturn(Optional.of(parent));
		when(structureRepository.findByLeftLimitGreaterThanAndRightLimitLessThan(Mockito.anyInt(), Mockito.anyInt()))
				.thenReturn(toMoveDescendants);
		Assertions.assertThrows(RuntimeException.class, () -> structureService.changeParent(1, 2));
	}

	@Test
	void shouldCreateCorrectStructure_whenReceiveNormalNewTree() throws IOException {
		Item root = objectMapper.readValue(TestHelper.loadStructureFromFile("normal"), Item.class);
		structureService.insertNewStructure(root);

		assertThat(root.getId()).isEqualTo(1l);
		assertThat(root.getLeftLimit()).isEqualTo(1);
		assertThat(root.getRightLimit()).isEqualTo(104);
		assertThat(root.getLevel()).isEqualTo(0);
		assertThat(root.getParent()).isNull();

		Item item = root.getChildren().get(0);
		assertThat(item.getId()).isEqualTo(2l);
		assertThat(item.getLeftLimit()).isEqualTo(2);
		assertThat(item.getRightLimit()).isEqualTo(77);
		assertThat(item.getLevel()).isEqualTo(1);
		assertThat(item.getParent().getId()).isEqualTo(1);

		item = root.getChildren().get(0).getChildren().get(0);
		assertThat(item.getId()).isEqualTo(3l);
		assertThat(item.getLeftLimit()).isEqualTo(3);
		assertThat(item.getRightLimit()).isEqualTo(72);
		assertThat(item.getLevel()).isEqualTo(2);
		assertThat(item.getParent().getId()).isEqualTo(2);

		item = root.getChildren().get(0).getChildren().get(0).getChildren().get(0);
		assertThat(item.getId()).isEqualTo(4l);
		assertThat(item.getLeftLimit()).isEqualTo(4);
		assertThat(item.getRightLimit()).isEqualTo(49);
		assertThat(item.getLevel()).isEqualTo(3);
		assertThat(item.getParent().getId()).isEqualTo(3);

		item = root.getChildren().get(0).getChildren().get(0).getChildren().get(0).getChildren().get(0);
		assertThat(item.getId()).isEqualTo(5l);
		assertThat(item.getLeftLimit()).isEqualTo(5);
		assertThat(item.getRightLimit()).isEqualTo(38);
		assertThat(item.getLevel()).isEqualTo(4);
		assertThat(item.getParent().getId()).isEqualTo(4);

		item = root.getChildren().get(0).getChildren().get(0).getChildren().get(0).getChildren().get(0).getChildren()
				.get(0);
		assertThat(item.getId()).isEqualTo(6l);
		assertThat(item.getLeftLimit()).isEqualTo(6);
		assertThat(item.getRightLimit()).isEqualTo(29);
		assertThat(item.getLevel()).isEqualTo(5);
		assertThat(item.getParent().getId()).isEqualTo(5);

		item = root.getChildren().get(0).getChildren().get(0).getChildren().get(3).getChildren().get(0).getChildren()
				.get(0).getChildren().get(0).getChildren().get(0);
		assertThat(item.getId()).isEqualTo(49l);
		assertThat(item.getLeftLimit()).isEqualTo(58);
		assertThat(item.getRightLimit()).isEqualTo(65);
		assertThat(item.getLevel()).isEqualTo(7);
		assertThat(item.getParent().getId()).isEqualTo(48);

		item = item.getChildren().get(0);
		assertThat(item.getId()).isEqualTo(50l);
		assertThat(item.getLeftLimit()).isEqualTo(59);
		assertThat(item.getRightLimit()).isEqualTo(64);
		assertThat(item.getLevel()).isEqualTo(8);
		assertThat(item.getParent().getId()).isEqualTo(49);

		item = item.getChildren().get(0);
		assertThat(item.getId()).isEqualTo(51l);
		assertThat(item.getLeftLimit()).isEqualTo(60);
		assertThat(item.getRightLimit()).isEqualTo(63);
		assertThat(item.getLevel()).isEqualTo(9);
		assertThat(item.getParent().getId()).isEqualTo(50);

		item = item.getChildren().get(0);
		assertThat(item.getId()).isEqualTo(52l);
		assertThat(item.getLeftLimit()).isEqualTo(61);
		assertThat(item.getRightLimit()).isEqualTo(62);
		assertThat(item.getLevel()).isEqualTo(10);
		assertThat(item.getParent().getId()).isEqualTo(51);

	}

}
