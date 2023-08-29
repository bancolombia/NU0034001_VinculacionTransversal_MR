package co.com.bancolombia.model.dependentfield;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class MyClassDependent {
	String myField;
	String currentOperation;
	String currentField;
	String dependentOperation;
	String dependentField;
	boolean mandatory;
}