package com.github.hatimiti.doxsl.core;

import static java.lang.String.*;

import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.jxls.area.Area;
import org.jxls.builder.AreaBuilder;
import org.jxls.builder.xls.XlsCommentAreaBuilder;
import org.jxls.common.CellRef;
import org.jxls.common.Context;
import org.jxls.transform.Transformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.hatimiti.doxsl.util.ClassPathResourceLoader;



public class Doxls implements Closeable {

	private static final Logger LOG = LoggerFactory.getLogger(Doxls.class);

	private InputStream is;
	private OutputStream os;
	private Transformer transformer;
	private AreaBuilder areaBuilder;
	private Context context;

	private List<Area> areas;

	public Doxls(String templatePath, File outputFile) throws FileNotFoundException {
		Objects.requireNonNull(templatePath);
		Objects.requireNonNull(outputFile);
		try {
			os = new FileOutputStream(outputFile);
		} catch (FileNotFoundException e) {
			LOG.error(format("outputFile=%s", outputFile), e);
			throw e;
		}
		init(templatePath);
	}

	public Doxls(String templatePath, OutputStream out) {
		Objects.requireNonNull(templatePath);
		Objects.requireNonNull(out);
		os = new BufferedOutputStream(out);
		init(templatePath);
	}

	private void init(String templatePath) {
		try {
			is = ClassPathResourceLoader
					.load(templatePath)
					.orElseThrow(FileNotFoundException::new);
			transformer = DoxlsTransformer.createTransformer(is, os);
		} catch (InvalidFormatException | IOException e) {
			LOG.error(format("is=%s, os=%s", is, os), e);
			throw new RuntimeException(e);
		}
        areaBuilder = new XlsCommentAreaBuilder(transformer);
        context = new Context();
	}

	private List<Area> getAreas() {
		if (areas == null) {
			areas = this.areaBuilder.build();
		}
		return areas;
	}

	private Optional<Area> getArea(int i) {
		List<Area> areas = getAreas();
		if (areas.size() <= 0) {
			return Optional.empty();
		}
		try {
			return Optional.ofNullable(areas.get(i));
		} catch (IndexOutOfBoundsException e) {
			return Optional.empty();
		}
	}

	public void applyAtArea(int i, String cellRef) {
		getArea(i).ifPresent(a -> a.applyAt(new CellRef(cellRef), context));
	}

	public void put(String name, Object value) {
		this.context.putVar(name, value);
	}

	public void put(Map<String, Object> values) {
		values.entrySet().stream().forEach(e -> {
			this.context.putVar(e.getKey(), e.getValue());
		});
	}

	public void output() {
		try {
			transformer.write();
		} catch (IOException e) {
			LOG.error(format("transformer=%s", transformer), e);
			throw new RuntimeException(e);
		}
	}

	public void removeSheetByNameOf(String name) {
		doxslTransformer().deleteSheet(name);
	}

	private DoxlsTransformer doxslTransformer() {
		if (!(this.transformer instanceof DoxlsTransformer)) {
			throw new IllegalStateException("transformer is not DxslTransformer.");
		}
		return (DoxlsTransformer) this.transformer;
	}

	@Override
	public void close() throws IOException {
		if (is != null) {
			is.close();
		}
		if (os != null) {
			os.close();
		}
	}

}
