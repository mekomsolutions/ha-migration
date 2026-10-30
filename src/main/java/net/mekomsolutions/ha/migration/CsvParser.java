package net.mekomsolutions.ha.migration;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Component;

import tools.jackson.databind.MappingIterator;
import tools.jackson.dataformat.csv.CsvMapper;
import tools.jackson.dataformat.csv.CsvSchema;

/**
 * Utility class for parsing CSV files into objects of a specified type. The class uses Jackson's
 * CsvMapper for handling the deserialization of CSV data.
 */
@Component
public class CsvParser {
	
	private static final CsvMapper MAPPER = CsvMapper.builder().build();
	
	private static CsvSchema SCHEMA = CsvSchema.emptySchema().withHeader();
	
	public final static String ATTRIB_DATE = "date";
	
	/**
	 * Parses the content of a CSV file and maps it to a list of objects of the specified type. The
	 * method uses Jackson's CsvMapper to handle the deserialization process.
	 *
	 * @param <T> the type of objects to map the CSV data to
	 * @param path the path to the CSV file to be parsed
	 * @param clazz the class type to which each row of the CSV should be mapped
	 * @return a MappingIterator that can be used to iterate over the deserialized objects
	 */
	public <T> MappingIterator<T> parse(Path path, Class<T> clazz) {
		return MAPPER.readerFor(clazz).with(SCHEMA)
		        .withAttribute(ATTRIB_DATE, LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)).readValues(path);
	}
	
}
