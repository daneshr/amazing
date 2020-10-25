package com.tradeshift.amazing;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tradeshift.amazing.dto.Tree;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class RestAPITests {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void contextLoads() {
	}

	@Test
	void shouldReturn5xx_whenReceiveNewTreeWithRepeatedIds() throws Exception {

		String payload = TestHelper.loadStructureFromFile("malformed-repeatedId");
		mockMvc.perform(post("/api/tree").contentType("application/json").content(payload))
				.andExpect(status().is5xxServerError());

	}

	@Test
	void shouldReturn2xx_whenReceiveNewTreeWithStringId() throws Exception {

		String payload = TestHelper.loadStructureFromFile("stringId");
		mockMvc.perform(post("/api/tree").contentType("application/json").content(payload))
				.andExpect(status().is2xxSuccessful());

	}

	@Test
	void shouldReturn4xx_whenReceiveEmptyTree() throws Exception {

		String payload = TestHelper.loadStructureFromFile("empty");
		mockMvc.perform(post("/api/tree").contentType("application/json").content(payload))
				.andExpect(status().is4xxClientError());

	}

	@Test
	void shouldKeepTheStructure_whenReceiveHorizontalNewTree() throws Exception {

		String payload = TestHelper.loadStructureFromFile("horizontal");
		mockMvc.perform(post("/api/tree").contentType("application/json").content(payload))
				.andExpect(status().is2xxSuccessful());

		String result = mockMvc.perform(get("/api/tree")).andExpect(status().is2xxSuccessful()).andReturn()
				.getResponse().getContentAsString();
		Tree responseTree = objectMapper.readValue(result, Tree.class);
		assertThat(responseTree.getLevel() == 0);
		assertThat(responseTree.getChildren().size()).isEqualTo(12);
		responseTree.getChildren().sort((a, b) -> Integer.valueOf(a.getName()) - Integer.valueOf(b.getName()));
		for (int id = 0; id < 12; id++) {
			assertThat(responseTree.getChildren().get(id).getLevel() == 1).isTrue();
			assertThat(responseTree.getChildren().get(id).getName().compareTo(Long.valueOf(id + 2).toString()) == 0)
					.isTrue();
			assertThat(responseTree.getChildren().get(id).getChildren()).isNull();
		}
	}

	@Test
	void shouldKeepTheStructure_whenReceiveVerticalNewTree() throws Exception {

		String payload = TestHelper.loadStructureFromFile("vertical");
		mockMvc.perform(post("/api/tree").contentType("application/json").content(payload))
				.andExpect(status().is2xxSuccessful());

		String result = mockMvc.perform(get("/api/tree")).andExpect(status().is2xxSuccessful()).andReturn()
				.getResponse().getContentAsString();
		Tree responseTree = objectMapper.readValue(result, Tree.class);
		for (int level = 0, id = 1; level < 9; id++, level++) {
			assertThat(responseTree.getLevel() == level).isTrue();
			assertThat(Integer.valueOf(responseTree.getName()) == id).isTrue();
			if (level < 8) {
				assertThat(responseTree.getChildren().size()).isEqualTo(1);
				responseTree = responseTree.getChildren().get(0);
			}
		}
	}

	@Test
	void shouldReturn4xx_whenReceiveIllegalInputForChangeParent() throws Exception {

		String payload = TestHelper.loadStructureFromFile("normal");
		mockMvc.perform(post("/api/tree").contentType("application/json").content(payload))
				.andExpect(status().is2xxSuccessful());
		mockMvc.perform(put("/api/tree/{nodeId}/move/{newParentId}", 1l, 4l)).andExpect(status().is4xxClientError());

	}

	@Test
	void shouldChangeParent_whenReceiveNormalInputForChangeParent() throws Exception {

		String payload = TestHelper.loadStructureFromFile("normal");
		mockMvc.perform(post("/api/tree").contentType("application/json").content(payload))
				.andExpect(status().is2xxSuccessful());
		mockMvc.perform(put("/api/tree/{nodeId}/move/{newParentId}", 7l, 1l)).andExpect(status().is2xxSuccessful());
		String result = mockMvc.perform(get("/api/tree")).andExpect(status().is2xxSuccessful()).andReturn()
				.getResponse().getContentAsString();
		Tree responseTree = objectMapper.readValue(result, Tree.class);
		assertThat(responseTree.getName().compareTo("1")).isEqualTo(0);
		assertThat(responseTree.getLevel()).isEqualTo(0);
		assertThat(responseTree.getChildren().size()).isEqualTo(5);

		assertThat(responseTree.getChildren().get(0).getName().compareTo("7")).isEqualTo(0);
		assertThat(responseTree.getChildren().get(0).getLevel()).isEqualTo(1);
		assertThat(responseTree.getChildren().get(0).getChildren().size()).isEqualTo(5);
		assertThat(responseTree.getChildren().get(0).getChildren().get(0).getName().compareTo("8")).isEqualTo(0);
		assertThat(responseTree.getChildren().get(0).getChildren().get(0).getLevel()).isEqualTo(2);

		assertThat(responseTree.getChildren().get(1).getChildren().get(0).getChildren().get(0).getChildren().get(0)
				.getChildren().get(0).getChildren().size()).isEqualTo(1);

	}

	@Test
	void shouldReturnCorrectDescendants_whenReceiveNormalInput() throws Exception {
		String payload = TestHelper.loadStructureFromFile("normal");
		mockMvc.perform(post("/api/tree").contentType("application/json").content(payload))
				.andExpect(status().is2xxSuccessful());

		String result = mockMvc.perform(get("/api/tree/{id}/structure", 7l)).andExpect(status().is2xxSuccessful())
				.andReturn().getResponse().getContentAsString();
		Tree responseTree = objectMapper.readValue(result, Tree.class);
		assertThat(responseTree.getName().compareTo("7")).isEqualTo(0);
		assertThat(responseTree.getLevel()).isEqualTo(6);
		assertThat(responseTree.getChildren().size()).isEqualTo(5);

		assertThat(responseTree.getChildren().get(0).getName().compareTo("8")).isEqualTo(0);
		assertThat(responseTree.getChildren().get(0).getLevel()).isEqualTo(7);

		assertThat(responseTree.getChildren().get(1).getName().compareTo("11")).isEqualTo(0);
		assertThat(responseTree.getChildren().get(1).getLevel()).isEqualTo(7);

		assertThat(responseTree.getChildren().get(2).getName().compareTo("12")).isEqualTo(0);
		assertThat(responseTree.getChildren().get(2).getLevel()).isEqualTo(7);

		assertThat(responseTree.getChildren().get(3).getName().compareTo("13")).isEqualTo(0);
		assertThat(responseTree.getChildren().get(3).getLevel()).isEqualTo(7);

		assertThat(responseTree.getChildren().get(4).getName().compareTo("14")).isEqualTo(0);
		assertThat(responseTree.getChildren().get(4).getLevel()).isEqualTo(7);

	}
}
