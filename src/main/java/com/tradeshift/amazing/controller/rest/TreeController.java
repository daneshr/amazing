package com.tradeshift.amazing.controller.rest;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.tradeshift.amazing.dto.ErrorMessageContainer;
import com.tradeshift.amazing.dto.Tree;
import com.tradeshift.amazing.model.Item;
import com.tradeshift.amazing.service.StructureService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Log4j2
public class TreeController {

	private final StructureService structureService;

	@ExceptionHandler({ ResponseStatusException.class })
	public ResponseEntity<ErrorMessageContainer> handleException(ResponseStatusException ex) {
		log.warn(String.format("error : %s", ex.getMessage()));
		return ResponseEntity.status(ex.getStatus())
				.body(ErrorMessageContainer.builder().errorMessage(ex.getReason()).build());
	}

	@ExceptionHandler({ RuntimeException.class })
	public ResponseEntity<ErrorMessageContainer> handleGenericExceptions(RuntimeException ex) {
		log.warn(String.format("error : %s", ex.getMessage()));
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(ErrorMessageContainer.builder().errorMessage(ex.getMessage()).build());
	}

	@ApiOperation(value = "Retrieve an item")
	@ApiResponses(value = { @ApiResponse(code = 202, message = "Retrieve the item successfully "),
			@ApiResponse(code = 404, message = "Item not found"),
			@ApiResponse(code = 500, message = "Internal server error") })
	@GetMapping(value = "/tree/{id}")
	public ResponseEntity<Item> getItem(@PathVariable Long id) {
		log.info(String.format("get item with id:%d", id));
		Item item = structureService.getSingleItem(id);
		return ResponseEntity.accepted().body(item);
	}

	@ApiOperation(value = "Retrieve whole structure from root item")
	@ApiResponses(value = { @ApiResponse(code = 202, message = "Retrieve the structure successfully "),
			@ApiResponse(code = 204, message = "Structure is empty"),
			@ApiResponse(code = 500, message = "Internal server error") })
	@GetMapping(value = "/tree")
	public ResponseEntity<Tree> getWholeStructure() {
		return ResponseEntity.accepted().body(structureService.getWholeStructure());
	}

	@ApiOperation(value = "List of item's descendants with parent reference")
	@ApiResponses(value = { @ApiResponse(code = 202, message = "Successfull Response "),
			@ApiResponse(code = 404, message = "Item not found"),
			@ApiResponse(code = 500, message = "internal server error") })
	@GetMapping(value = "/tree/{id}/descendants")
	public ResponseEntity<List<Item>> getAllDescendants(@PathVariable Long id) {
		log.info(String.format("get all descendants request for item with id:%d", id));
		return ResponseEntity.accepted().body(structureService.getAllDescendantItems(id));
	}

	@ApiOperation(value = "Retrieve sub structure from given item")
	@ApiResponses(value = { @ApiResponse(code = 202, message = "Retrieve sub structure successfully "),
			@ApiResponse(code = 404, message = "Item not found"),
			@ApiResponse(code = 500, message = "Internal server error") })
	@GetMapping(value = "/tree/{id}/structure")
	public ResponseEntity<Tree> getStructure(@PathVariable Long id) {
		log.info(String.format("get structure request for item with id:%d", id));
		return ResponseEntity.accepted().body(structureService.getSubStructure(id));
	}

	@ApiOperation(value = "Replace new structure")
	@ApiResponses(value = { @ApiResponse(code = 202, message = "Operation was successful."),
			@ApiResponse(code = 400, message = "Id duplication"),
			@ApiResponse(code = 500, message = "Internal server error") })
	@PostMapping(value = "/tree")
	public ResponseEntity<String> insertNewStructure(@RequestBody Item root) {
		log.info(String.format("insert new structure request root id:%d", root.getId()));
		structureService.insertNewStructure(root);
		return ResponseEntity.accepted().body("Succeed!");
	}

	@ApiOperation(value = "Move and item to another parent")
	@ApiResponses(value = { @ApiResponse(code = 202, message = "Successful, move"),
			@ApiResponse(code = 404, message = "Item or new parent not found."),
			@ApiResponse(code = 409, message = "Item can not move to it's child"),
			@ApiResponse(code = 500, message = "Internal server error") })
	@PutMapping(value = "/tree/{nodeId}/move/{newParentId}")
	public ResponseEntity<String> changeParent(@PathVariable Long nodeId, @PathVariable Long newParentId) {
		log.info(String.format("change parent request, item id:%d to parent id:%d", nodeId, newParentId));
		structureService.changeParent(nodeId, newParentId);
		return ResponseEntity.accepted().body("Succeed!");
	}

}
