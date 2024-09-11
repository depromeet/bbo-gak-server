package com.server.bbo_gak.global;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

import com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper;
import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.Schema;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.snippet.Attributes;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

@Component
public class RestDocsFactory {

    public MockHttpServletRequestBuilder createRequest(
        String url, Object requestDto, HttpMethod method, ObjectMapper objectMapper
    ) throws Exception {
        String content = objectMapper.writeValueAsString(requestDto);
        return buildRequest(url, content, method);
    }

    public MockHttpServletRequestBuilder createRequest(
        String url, Object requestDto, HttpMethod method, ObjectMapper objectMapper, Object... pathParams
    ) throws Exception {
        String content = objectMapper.writeValueAsString(requestDto);
        return buildRequest(url, content, method, pathParams);
    }

    public MockHttpServletRequestBuilder createRequestList(
        String url, List<?> requestDtos, HttpMethod method, ObjectMapper objectMapper
    ) throws Exception {
        StringBuilder contentBuilder = new StringBuilder("[");
        for (int i = 0; i < requestDtos.size(); i++) {
            contentBuilder.append(objectMapper.writeValueAsString(requestDtos.get(i)));
            if (i < requestDtos.size() - 1) {
                contentBuilder.append(",");
            }
        }
        contentBuilder.append("]");
        String content = contentBuilder.toString();
        return buildRequest(url, content, method);
    }

    public MockHttpServletRequestBuilder createRequestList(
        String url, List<?> requestDtos, HttpMethod method, ObjectMapper objectMapper, Object... pathParams
    ) throws Exception {
        StringBuilder contentBuilder = new StringBuilder("[");
        for (int i = 0; i < requestDtos.size(); i++) {
            contentBuilder.append(objectMapper.writeValueAsString(requestDtos.get(i)));
            if (i < requestDtos.size() - 1) {
                contentBuilder.append(",");
            }
        }
        contentBuilder.append("]");
        String content = contentBuilder.toString();
        return buildRequest(url, content, method, pathParams);
    }

    public <T, R> RestDocumentationResultHandler getSuccessResource(
        String identifier, String description, String tag, T requestDto, R responseDto
    ) {
        String requestSchemaName = requestDto != null ? requestDto.getClass().getSimpleName() : "";
        String responseSchemaName = responseDto != null ? responseDto.getClass().getSimpleName() : "";

        return getRestDocumentationResultHandler(identifier, description, tag, requestDto, responseDto,
            requestSchemaName, responseSchemaName);
    }

    private <T, R> RestDocumentationResultHandler getRestDocumentationResultHandler(String identifier,
        String description, String tag, T requestDto, R responseDto, String requestSchemaName,
        String responseSchemaName) {

        if (requestDto == null) {
            return MockMvcRestDocumentationWrapper.document(identifier,
                preprocessResponse(prettyPrint()),
                resource(
                    ResourceSnippetParameters.builder()
                        .tags(tag)
                        .description(description)
                        .responseSchema(Schema.schema(responseSchemaName))
                        .responseFields(responseDto != null ? getFields(responseDto) : new FieldDescriptor[]{})
                        .build()
                )
            );
        }

        return MockMvcRestDocumentationWrapper.document(identifier,
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            resource(
                ResourceSnippetParameters.builder()
                    .tags(tag)
                    .description(description)
                    .requestSchema(Schema.schema(requestSchemaName))
                    .responseSchema(Schema.schema(responseSchemaName))
                    .requestFields(getFields(requestDto))
                    .responseFields(responseDto != null ? getFields(responseDto) : new FieldDescriptor[]{})
                    .build()
            )
        );
    }

    public <T, R> RestDocumentationResultHandler getSuccessResourceList(
        String identifier, String description, String tag, List<T> requestDtos, List<R> responseDtos
    ) {
        String requestSchemaName = !requestDtos.isEmpty() ? requestDtos.getFirst().getClass().getSimpleName() : "";
        String responseSchemaName = !responseDtos.isEmpty() ? responseDtos.getFirst().getClass().getSimpleName() : "";

        return getRestDocumentationResultHandler(identifier, description, tag, requestDtos, responseDtos,
            requestSchemaName,
            responseSchemaName);
    }

    private <T, R> RestDocumentationResultHandler getRestDocumentationResultHandler(String identifier,
        String description, String tag,
        List<T> requestDtos, List<R> responseDtos, String requestSchemaName, String responseSchemaName) {

        if (requestDtos.isEmpty()) {
            return MockMvcRestDocumentationWrapper.document(identifier,
                preprocessResponse(prettyPrint()),
                resource(
                    ResourceSnippetParameters.builder()
                        .tags(tag)
                        .description(description)
                        .responseSchema(Schema.schema(responseSchemaName))
                        .responseFields(
                            getFieldsList(
                                !responseDtos.isEmpty() ? responseDtos.getFirst() : new FieldDescriptor[]{}))
                        .build()
                )
            );
        }

        return MockMvcRestDocumentationWrapper.document(identifier,
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            resource(
                ResourceSnippetParameters.builder()
                    .tags(tag)
                    .description(description)
                    .requestSchema(Schema.schema(requestSchemaName))
                    .responseSchema(Schema.schema(responseSchemaName))
                    .requestFields(getFieldsList(requestDtos.getFirst()))
                    .responseFields(getFieldsList(
                        !responseDtos.isEmpty() ? responseDtos.getFirst() : new FieldDescriptor[]{}))
                    .build()
            )
        );
    }


    public <T> RestDocumentationResultHandler getFailureResourceList(String identifier, String tag,
        List<T> requestDtos) {

        String requestSchemaName = !requestDtos.isEmpty() ? requestDtos.getFirst().getClass().getSimpleName() : "";

        return getRestDocumentationResultHandler(identifier, tag, requestDtos, requestSchemaName);
    }

    private <T> RestDocumentationResultHandler getRestDocumentationResultHandler(String identifier, String tag,
        List<T> requestDtos, String requestSchemaName) {

        if (requestDtos.isEmpty()) {
            return MockMvcRestDocumentationWrapper.document(identifier,
                preprocessResponse(prettyPrint()),
                resource(
                    ResourceSnippetParameters.builder()
                        .tags(tag)
                        .responseFields(getFailureFields())
                        .build()
                )
            );
        }
        return MockMvcRestDocumentationWrapper.document(identifier,
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            resource(
                ResourceSnippetParameters.builder()
                    .tags(tag)
                    .requestSchema(Schema.schema(requestSchemaName))
                    .requestFields(getFieldsList(requestDtos.getFirst()))
                    .responseFields(getFailureFields())
                    .build()
            )
        );
    }

    public <T> RestDocumentationResultHandler getFailureResource(String identifier, String tag, T requestDto) {
        String requestSchemaName = requestDto != null ? requestDto.getClass().getSimpleName() : "";

        return getRestDocumentationResultHandler(identifier, tag, requestDto, requestSchemaName);
    }

    private <T> RestDocumentationResultHandler getRestDocumentationResultHandler(String identifier, String tag,
        T requestDto, String requestSchemaName) {
        if (requestDto == null) {
            return MockMvcRestDocumentationWrapper.document(identifier,
                preprocessResponse(prettyPrint()),
                resource(
                    ResourceSnippetParameters.builder()
                        .tags(tag)
                        .responseFields(getFailureFields())
                        .build()
                )
            );
        }
        return MockMvcRestDocumentationWrapper.document(identifier,
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            resource(
                ResourceSnippetParameters.builder()
                    .tags(tag)
                    .requestSchema(Schema.schema(requestSchemaName))
                    .requestFields(getFields(requestDto))
                    .responseFields(getFailureFields())
                    .build()
            )
        );
    }

    private MockHttpServletRequestBuilder buildRequest(String url, String content, HttpMethod method) {
        return switch (method.name()) {
            case "POST" -> RestDocumentationRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept(MediaType.APPLICATION_JSON);
            case "GET" -> RestDocumentationRequestBuilders.get(url)
                .contentType(MediaType.APPLICATION_JSON);
            case "PUT" -> RestDocumentationRequestBuilders.put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept(MediaType.APPLICATION_JSON);
            case "DELETE" -> content.equals("null") ?
                RestDocumentationRequestBuilders.delete(url)
                    .contentType(MediaType.APPLICATION_JSON)
                :
                    RestDocumentationRequestBuilders.delete(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content)
                        .accept(MediaType.APPLICATION_JSON);
            default -> throw new IllegalArgumentException("Invalid HTTP method: " + method);
        };
    }

    public MockHttpServletRequestBuilder buildRequest(String url, String content, HttpMethod method,
        Object... pathParams) {
        return switch (method.name()) {
            case "POST" -> createPostRequest(url, content, pathParams);
            case "GET" -> createGetRequest(url, content, pathParams);
            case "PUT" -> createPutRequest(url, content, pathParams);
            case "PATCH" -> createPatchRequest(url, content, pathParams);
            case "DELETE" -> createDeleteRequest(url, content, pathParams);
            default -> throw new IllegalArgumentException("Invalid HTTP method: " + method);
        };
    }

    private MockHttpServletRequestBuilder createPostRequest(String url, String content, Object... pathParams) {
        return RestDocumentationRequestBuilders.post(url, pathParams)
            .contentType(MediaType.APPLICATION_JSON)
            .content(content)
            .accept(MediaType.APPLICATION_JSON);
    }

    private MockHttpServletRequestBuilder createPatchRequest(String url, String content, Object... pathParams) {
        return RestDocumentationRequestBuilders.patch(url, pathParams)
            .contentType(MediaType.APPLICATION_JSON)
            .content(content)
            .accept(MediaType.APPLICATION_JSON);
    }

    private MockHttpServletRequestBuilder createGetRequest(String url, String content, Object... pathParams) {
        return RestDocumentationRequestBuilders.get(url, pathParams)
            .contentType(MediaType.APPLICATION_JSON);
    }

    private MockHttpServletRequestBuilder createPutRequest(String url, String content, Object... pathParams) {
        return RestDocumentationRequestBuilders.put(url, pathParams)
            .contentType(MediaType.APPLICATION_JSON)
            .content(content)
            .accept(MediaType.APPLICATION_JSON);
    }

    private MockHttpServletRequestBuilder createDeleteRequest(String url, String content, Object... pathParams) {
        if (content.equals("null")) {
            return RestDocumentationRequestBuilders.delete(url, pathParams)
                .contentType(MediaType.APPLICATION_JSON);
        }
        return RestDocumentationRequestBuilders.delete(url, pathParams)
            .contentType(MediaType.APPLICATION_JSON)
            .content(content)
            .accept(MediaType.APPLICATION_JSON);
    }

    private FieldDescriptor[] getFailureFields() {
        return new FieldDescriptor[]{
            fieldWithPath("message").type(JsonFieldType.STRING).description("에러 메시지"),
            fieldWithPath("status").type(JsonFieldType.STRING).description("HTTP 상태 코드")
        };
    }

    public <T> FieldDescriptor[] getFields(T dto) {
        List<FieldDescriptor> fields = new ArrayList<>();
        generateFieldDescriptors(dto, "", fields);
        return fields.toArray(new FieldDescriptor[0]);
    }

    public <T> FieldDescriptor[] getFieldsList(T dto) {
        List<FieldDescriptor> fields = new ArrayList<>();
        generateFieldDescriptors(dto, "[].", fields);
        return fields.toArray(new FieldDescriptor[0]);
    }

    private <T> void generateFieldDescriptors(T dto, String pathPrefix, List<FieldDescriptor> fields) {
        if (isSimpleType(dto)) {
            return;
        }

        Field[] declaredFields = dto.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            String fieldPath = pathPrefix + field.getName();
            JsonFieldType fieldType = determineFieldType(field.getType());
            Object fieldValue = getFieldValue(dto, field);

            FieldDescriptor descriptor = fieldWithPath(fieldPath)
                .type(fieldType)
                .description(field.getName())
                .optional();

            if (fieldType != JsonFieldType.OBJECT && fieldType != JsonFieldType.ARRAY) {
                descriptor.attributes(
                    Attributes.key("example").value(fieldValue != null ? fieldValue.toString() : "null"));
            }

            fields.add(descriptor);

            // 리스트 타입 처리
            if (fieldType == JsonFieldType.ARRAY && fieldValue instanceof List<?> list) {
                if (!list.isEmpty()) {
                    Object firstElement = list.getFirst();
                    generateFieldDescriptors(firstElement, fieldPath + "[].", fields);
                }
            }

            // 오브젝트 타입 처리
            if (fieldType == JsonFieldType.OBJECT && fieldValue != null) {
                generateFieldDescriptors(fieldValue, fieldPath + ".", fields);
            }
        }
    }

    private <T> Object getFieldValue(T dto, Field field) {
        try {
            return field.get(dto);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to access field: " + field.getName(), e);
        }
    }

    private boolean isSimpleType(Object dto) {
        return dto instanceof String || dto instanceof Number || dto instanceof Boolean || dto.getClass().isEnum();
    }

    private JsonFieldType determineFieldType(Class<?> fieldType) {
        if (fieldType == String.class || fieldType.isEnum()) {
            return JsonFieldType.STRING;
        } else if (Boolean.class.isAssignableFrom(fieldType) || fieldType == boolean.class) {
            return JsonFieldType.BOOLEAN;
        } else if (Number.class.isAssignableFrom(fieldType) || fieldType.isPrimitive()) {
            return JsonFieldType.NUMBER;
        } else if (List.class.isAssignableFrom(fieldType)) {
            return JsonFieldType.ARRAY;
        } else {
            return JsonFieldType.OBJECT;
        }
    }

    private FieldDescriptor fieldWithPath(String path) {
        return PayloadDocumentation.fieldWithPath(path);
    }

}
