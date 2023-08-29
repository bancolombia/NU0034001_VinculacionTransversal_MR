package co.com.bancolombia.model.dependentfield.gateways;

import co.com.bancolombia.commonsvnt.model.dependentfield.DependentField;

import java.util.List;
import java.util.UUID;

public interface DependentFieldRepository {
	List<DependentField> findByTypeAcquisitionAndCurrentFieldAndActive(
			String typeAcquisition, String currentField, boolean active);

	List<DependentField> findByTypeAcquisitionAndCurrentOperationAndCurrentFieldInAndActive(
			String typeAcquisition, String currentOperation, List<String> currentFields, boolean active);

	List<Object> searchDependentValueFieldAndActive(
    		UUID idAcq, String table, String searchField, String searchValue, boolean active);

    List<DependentField> findByTypeAcquisitionAndCurrentOperationAndActive(
			String typeAcquisition, String currentOperation, boolean active);
}