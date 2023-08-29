package co.com.bancolombia.usecase.contactinformation.generic;

import co.com.bancolombia.commonsvnt.common.exception.ErrorField;
import co.com.bancolombia.commonsvnt.rabbit.vinculation.reply.ExecFieldReply;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Builder
@Data
@AllArgsConstructor
public class ArrayErrors<C> {
    public final List<ExecFieldReply> mandatoryExecFList;
    public final List<ErrorField> errorFieldsMerge;
    public final List<C> cInfoUpd;
    public final List<ErrorField> errorFieldsValidate;

    public ArrayErrors(List<ExecFieldReply> mandatoryExecFList) {
        this.mandatoryExecFList = mandatoryExecFList;
        this.errorFieldsMerge = new ArrayList<>();
        this.cInfoUpd = new ArrayList<>();
        this.errorFieldsValidate = new ArrayList<>();
    }

}
