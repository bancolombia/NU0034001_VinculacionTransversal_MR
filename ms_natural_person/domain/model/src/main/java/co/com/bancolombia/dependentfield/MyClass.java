package co.com.bancolombia.dependentfield;

import lombok.Builder;
import lombok.Data;

import java.beans.PropertyDescriptor;

@Builder
@Data
public class MyClass {
    public String myKey;
    public PropertyDescriptor myMethod;
}
