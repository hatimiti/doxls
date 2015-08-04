package com.github.hatimiti.doxsl.util;

import java.io.InputStream;
import java.util.Optional;

public class ClassPathResourceLoader {

	public static Optional<InputStream> load(String path) {
		return Optional.ofNullable(ClassLoader.getSystemResourceAsStream(path));
	}

}
