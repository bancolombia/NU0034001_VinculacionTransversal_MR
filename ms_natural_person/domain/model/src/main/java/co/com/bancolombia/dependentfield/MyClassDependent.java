package co.com.bancolombia.dependentfield;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class MyClassDependent {
	String myField;
	boolean mandatory;
	String currentOperation;
	String currentField;
	String dependentOperation;
	String dependentField;
}