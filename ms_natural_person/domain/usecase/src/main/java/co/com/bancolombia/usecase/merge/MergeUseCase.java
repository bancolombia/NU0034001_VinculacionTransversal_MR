package co.com.bancolombia.usecase.merge;

import co.com.bancolombia.common.interfaces.Mergeable;
import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.model.merge.MergeAttrib;

import java.beans.PropertyDescriptor;
import java.util.List;

public interface MergeUseCase {
    List<ErrorField> merge(
            Mergeable oldData, Mergeable newData, MergeAttrib mergeAttrib);

    List<PropertyDescriptor> getAnnotatedFieldsProperties(
            Mergeable oldData);
}