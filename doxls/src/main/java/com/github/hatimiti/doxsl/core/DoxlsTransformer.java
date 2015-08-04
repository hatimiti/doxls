package com.github.hatimiti.doxsl.core;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.jxls.common.AreaRef;
import org.jxls.common.CellData;
import org.jxls.common.CellRef;
import org.jxls.common.Context;
import org.jxls.common.ImageType;
import org.jxls.transform.AbstractTransformer;
import org.jxls.transform.poi.PoiTransformer;

/**
 * Extended org.jxls.transform.poi.PoiTransformer
 * @author hatimiti
 * @see org.jxls.transform.poi.PoiTransformer
 */
class DoxlsTransformer extends AbstractTransformer {

	protected PoiTransformer transformer;

	private DoxlsTransformer(PoiTransformer transformer) {
		this.transformer = transformer;
	}

	public static DoxlsTransformer createTransformer(InputStream is, OutputStream os) throws IOException, InvalidFormatException {
		return new DoxlsTransformer(PoiTransformer.createTransformer(is, os));
	}

	public static DoxlsTransformer createTransformer(InputStream is) throws IOException, InvalidFormatException {
		return new DoxlsTransformer(PoiTransformer.createTransformer(is));
	}

	public static DoxlsTransformer createTransformer(Workbook workbook) {
		return new DoxlsTransformer(PoiTransformer.createTransformer(workbook));
	}

	@Override
	public void transform(CellRef srcCellRef, CellRef targetCellRef,
			Context context) {
		transformer.transform(srcCellRef, targetCellRef, context);
	}

	@Override
	public void setFormula(CellRef cellRef, String formulaString) {
		transformer.setFormula(cellRef, formulaString);
	}

	@Override
	public void clearCell(CellRef cellRef) {
		transformer.clearCell(cellRef);
	}

	@Override
	public List<CellData> getCommentedCells() {
		return transformer.getCommentedCells();
	}

	@Override
	public void addImage(AreaRef areaRef, byte[] imageBytes, ImageType imageType) {
		transformer.addImage(areaRef, imageBytes, imageType);
	}

	@Override
	public void write() throws IOException {
		transformer.write();
	}

	@Override
	public void deleteSheet(String sheetName) {
		transformer.deleteSheet(sheetName);
	}

	@Override
	public void setHidden(String sheetName, boolean hidden) {
		transformer.setHidden(sheetName, hidden);
	}

	/*
	 * Follow Extended
	 */

	public Workbook getWorkbook() {
		return transformer.getWorkbook();
	}

	void deleteSheetAt(int i) {
		getWorkbook().removeSheetAt(i);
	}

}
