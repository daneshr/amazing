package com.tradeshift.amazing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.tradeshift.amazing.model.Item;
import com.tradeshift.amazing.repository.CompanyStructureRepository;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class JPATests {
	@Autowired
	private MockMvc mockMvc;

	@Test
	public void contextLoads() {
	}

	@Autowired
	private CompanyStructureRepository structureRepository;

	@Test
	void shouldHaveCorrectLeftRightLevel_whenReceiveNewTree() throws Exception {

		String payload = TestHelper.loadStructureFromFile("normal");
		mockMvc.perform(post("/api/tree").contentType("application/json").content(payload))
				.andExpect(status().is2xxSuccessful());

		Item item = structureRepository.findById(1l).orElseThrow(() -> new RuntimeException());

		assertThat(item.getId()).isEqualTo(1l);
		assertThat(item.getLeftLimit()).isEqualTo(1);
		assertThat(item.getRightLimit()).isEqualTo(104);
		assertThat(item.getLevel()).isEqualTo(0);
		assertThat(item.getParent()).isNull();

		item = structureRepository.findById(2l).orElseThrow(() -> new RuntimeException());

		assertThat(item.getId()).isEqualTo(2l);
		assertThat(item.getLeftLimit()).isEqualTo(2);
		assertThat(item.getRightLimit()).isEqualTo(77);
		assertThat(item.getLevel()).isEqualTo(1);
		assertThat(item.getParent().getId()).isEqualTo(1);

		item = structureRepository.findById(3l).orElseThrow(() -> new RuntimeException());

		assertThat(item.getId()).isEqualTo(3l);
		assertThat(item.getLeftLimit()).isEqualTo(3);
		assertThat(item.getRightLimit()).isEqualTo(72);
		assertThat(item.getLevel()).isEqualTo(2);
		assertThat(item.getParent().getId()).isEqualTo(2);

		item = structureRepository.findById(4l).orElseThrow(() -> new RuntimeException());

		assertThat(item.getId()).isEqualTo(4l);
		assertThat(item.getLeftLimit()).isEqualTo(4);
		assertThat(item.getRightLimit()).isEqualTo(49);
		assertThat(item.getLevel()).isEqualTo(3);
		assertThat(item.getParent().getId()).isEqualTo(3);

		item = structureRepository.findById(5l).orElseThrow(() -> new RuntimeException());

		assertThat(item.getId()).isEqualTo(5l);
		assertThat(item.getLeftLimit()).isEqualTo(5);
		assertThat(item.getRightLimit()).isEqualTo(38);
		assertThat(item.getLevel()).isEqualTo(4);
		assertThat(item.getParent().getId()).isEqualTo(4);

		item = structureRepository.findById(6l).orElseThrow(() -> new RuntimeException());

		assertThat(item.getId()).isEqualTo(6l);
		assertThat(item.getLeftLimit()).isEqualTo(6);
		assertThat(item.getRightLimit()).isEqualTo(29);
		assertThat(item.getLevel()).isEqualTo(5);
		assertThat(item.getParent().getId()).isEqualTo(5);

		item = structureRepository.findById(7l).orElseThrow(() -> new RuntimeException());

		assertThat(item.getId()).isEqualTo(7l);
		assertThat(item.getLeftLimit()).isEqualTo(7);
		assertThat(item.getRightLimit()).isEqualTo(26);
		assertThat(item.getLevel()).isEqualTo(6);
		assertThat(item.getParent().getId()).isEqualTo(6);

		item = structureRepository.findById(8l).orElseThrow(() -> new RuntimeException());

		assertThat(item.getId()).isEqualTo(8l);
		assertThat(item.getLeftLimit()).isEqualTo(8);
		assertThat(item.getRightLimit()).isEqualTo(13);
		assertThat(item.getLevel()).isEqualTo(7);
		assertThat(item.getParent().getId()).isEqualTo(7);

		item = structureRepository.findById(49l).orElseThrow(() -> new RuntimeException());

		assertThat(item.getId()).isEqualTo(49l);
		assertThat(item.getLeftLimit()).isEqualTo(58);
		assertThat(item.getRightLimit()).isEqualTo(65);
		assertThat(item.getLevel()).isEqualTo(7);
		assertThat(item.getParent().getId()).isEqualTo(48);

		item = structureRepository.findById(50l).orElseThrow(() -> new RuntimeException());

		assertThat(item.getId()).isEqualTo(50l);
		assertThat(item.getLeftLimit()).isEqualTo(59);
		assertThat(item.getRightLimit()).isEqualTo(64);
		assertThat(item.getLevel()).isEqualTo(8);
		assertThat(item.getParent().getId()).isEqualTo(49);

		item = structureRepository.findById(51l).orElseThrow(() -> new RuntimeException());

		assertThat(item.getId()).isEqualTo(51l);
		assertThat(item.getLeftLimit()).isEqualTo(60);
		assertThat(item.getRightLimit()).isEqualTo(63);
		assertThat(item.getLevel()).isEqualTo(9);
		assertThat(item.getParent().getId()).isEqualTo(50);

		item = structureRepository.findById(52l).orElseThrow(() -> new RuntimeException());

		assertThat(item.getId()).isEqualTo(52l);
		assertThat(item.getLeftLimit()).isEqualTo(61);
		assertThat(item.getRightLimit()).isEqualTo(62);
		assertThat(item.getLevel()).isEqualTo(10);
		assertThat(item.getParent().getId()).isEqualTo(51);

	}

}
