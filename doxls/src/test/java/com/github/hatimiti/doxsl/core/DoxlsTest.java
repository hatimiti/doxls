package com.github.hatimiti.doxsl.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.junit.Test;

public class DoxlsTest {

	public static final String TEMPLATE_BASE_PATH = "templates/report/xls/";

	@Test
	public void test() throws Exception {
		nestedCommandDemo();
	}

	protected static void nestedCommandDemo() throws Exception,
	FileNotFoundException {
		List<Employee> employees = new ArrayList<Employee>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MMM-dd", Locale.US);
		employees.add( new Employee("Elsa", dateFormat.parse("1970-Jul-10"), 1500, 0.15) );
		employees.add( new Employee("Oleg", dateFormat.parse("1973-Apr-30"), 2300, 0.25) );
		employees.add( new Employee("Neil", dateFormat.parse("1975-Oct-05"), 2500, 0.00) );
		employees.add( new Employee("Maria", dateFormat.parse("1978-Jan-07"), 1700, 0.15) );
		employees.add( new Employee("John", dateFormat.parse("1969-May-30"), 2800, 0.20) );

		try (Doxls xls = new Doxls(
				TEMPLATE_BASE_PATH + "nested_command_template.xls",
				new File("build/reports/nested_command_template.xls"))) {

			xls.put("employees", employees);
			xls.applyAtArea(0, "Result!A1");
			xls.output();
			xls.removeSheetByNameOf("Template");
		}
	}


}
