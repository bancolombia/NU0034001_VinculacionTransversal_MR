package co.com.bancolombia.api.response;

import co.com.bancolombia.api.model.generatexposedocuments.GenExpDocsRequest;
import co.com.bancolombia.api.model.generatexposedocuments.GenExpDocsResponse;
import co.com.bancolombia.api.model.generatexposedocuments.GenExpDocsResponseData;
import co.com.bancolombia.commonsvnt.api.model.util.MetaResponse;
import co.com.bancolombia.model.exposedocuments.ExposeDocs;

public class ResponseFactoryGenExpDocs {

    private ResponseFactoryGenExpDocs() {
    }

    public static GenExpDocsResponse buildExposeDocsResponse(GenExpDocsRequest request, ExposeDocs exposeDocs) {
        GenExpDocsResponseData data = GenExpDocsResponseData.builder().url(exposeDocs.getPreSignedUrl()).build();
        MetaResponse metaResponse = MetaResponse.fromMeta(request.getMeta());
        return GenExpDocsResponse.builder().data(data).meta(metaResponse).build();
    }
}
