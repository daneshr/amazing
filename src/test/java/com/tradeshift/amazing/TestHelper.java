package com.tradeshift.amazing;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.springframework.core.io.ClassPathResource;

public class TestHelper {

	public static String loadStructureFromFile(String fileName) throws IOException {
		File resource = new ClassPathResource("testStructures/" + fileName + ".json").getFile();
		return new String(Files.readAllBytes(resource.toPath()));
	}

}
