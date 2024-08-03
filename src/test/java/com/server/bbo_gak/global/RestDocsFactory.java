package com.server.bbo_gak.global;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;

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
import org.springframework.restdocs.snippet.Attributes;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.RequestBuilder;

@Component
public class RestDocsFactory {

    public RequestBuilder createRequest(
        String url, Object requestDto, HttpMethod method, ObjectMapper objectMapper
    ) throws Exception {
        String content = objectMapper.writeValueAsString(requestDto);
        return buildRequest(url, content, method);
    }

    public RequestBuilder createRequest(
        String url, Object requestDto, HttpMethod method, ObjectMapper objectMapper, Object... pathParams
    ) throws Exception {
        String content = objectMapper.writeValueAsString(requestDto);
        return buildRequest(url, content, method, pathParams);
    }

    public RequestBuilder createRequestList(
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

    public RequestBuilder createRequestList(
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
        String requestSchemaName = requestDto.getClass().getSimpleName();
        String responseSchemaName = responseDto != null ? responseDto.getClass().getSimpleName() : "";

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
        String requestSchemaName = requestDtos.getFirst().getClass().getSimpleName();
        String responseSchemaName = responseDtos.getFirst().getClass().getSimpleName();

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
                    .responseFields(getFieldsList(responseDtos.getFirst()))
                    .build()
            )
        );
    }


    public <T> RestDocumentationResultHandler getFailureResourceList(String identifier, String tag,
        List<T> requestDtos) {
//        List<FieldDescriptor> requestFields = new ArrayList<>();
//        for (T requestDto : requestDtos) {
//            requestFields.addAll(getFieldsList(requestDto));
//        }
        String requestSchemaName = requestDtos.getFirst().getClass().getSimpleName();

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
        String requestSchemaName = requestDto.getClass().getSimpleName();

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

    private RequestBuilder buildRequest(String url, String content, HttpMethod method) {
        return switch (method.name()) {
            case "POST" -> RestDocumentationRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept(MediaType.APPLICATION_JSON);
            case "GET" -> RestDocumentationRequestBuilders.get(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept(MediaType.APPLICATION_JSON);
            case "PUT" -> RestDocumentationRequestBuilders.put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept(MediaType.APPLICATION_JSON);
            case "DELETE" -> RestDocumentationRequestBuilders.delete(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .accept(MediaType.APPLICATION_JSON);
            default -> throw new IllegalArgumentException("Invalid HTTP method: " + method);
        };
    }

    public RequestBuilder buildRequest(String url, String content, HttpMethod method, Object... pathParams) {
        return switch (method.name()) {
            case "POST" -> createPostRequest(url, content, pathParams);
            case "GET" -> createGetRequest(url, content, pathParams);
            case "PUT" -> createPutRequest(url, content, pathParams);
            case "DELETE" -> createDeleteRequest(url, content, pathParams);
            default -> throw new IllegalArgumentException("Invalid HTTP method: " + method);
        };
    }

    private RequestBuilder createPostRequest(String url, String content, Object... pathParams) {
        return RestDocumentationRequestBuilders.post(url, pathParams)
            .contentType(MediaType.APPLICATION_JSON)
            .content(content)
            .accept(MediaType.APPLICATION_JSON);
    }

    private RequestBuilder createGetRequest(String url, String content, Object... pathParams) {
        return RestDocumentationRequestBuilders.get(url, pathParams)
            .contentType(MediaType.APPLICATION_JSON)
            .content(content)
            .accept(MediaType.APPLICATION_JSON);
    }

    private RequestBuilder createPutRequest(String url, String content, Object... pathParams) {
        return RestDocumentationRequestBuilders.put(url, pathParams)
            .contentType(MediaType.APPLICATION_JSON)
            .content(content)
            .accept(MediaType.APPLICATION_JSON);
    }

    private RequestBuilder createDeleteRequest(String url, String content, Object... pathParams) {
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

    private <T> FieldDescriptor[] getFields(T dto) {
        List<FieldDescriptor> fields = new ArrayList<>();
        Field[] declaredFields = dto.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            String name = field.getName();
            JsonFieldType type = determineFieldType(field.getType());
            Object value = getFieldValue(dto, field);

            fields.add(fieldWithPath(name)
                .type(type)
                .description(name)
                .attributes(Attributes.key("example").value(value != null ? value.toString() : "null"))); // 필드 값 추가
        }

        return fields.toArray(new FieldDescriptor[0]);
    }

    private <T> FieldDescriptor[] getFieldsList(T dto) {
        List<FieldDescriptor> fields = new ArrayList<>();
        Field[] declaredFields = dto.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            String name = field.getName();
            JsonFieldType type = determineFieldType(field.getType());
            Object value = getFieldValue(dto, field);

            // 배열 필드 경로 설정
            String path = "[]." + name;

            fields.add(fieldWithPath(path)
                .type(type)
                .description(name)
                .attributes(Attributes.key("example").value(value != null ? value.toString() : "null")));
        }

        return fields.toArray(new FieldDescriptor[0]);
    }

    private <T> Object getFieldValue(T dto, Field field) {
        try {
            return field.get(dto);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to access field: " + field.getName(), e);
        }
    }

    private JsonFieldType determineFieldType(Class<?> fieldType) {
        if (fieldType == String.class || fieldType.isEnum()) {
            return JsonFieldType.STRING;
        } else if (Number.class.isAssignableFrom(fieldType) || fieldType.isPrimitive()) {
            return JsonFieldType.NUMBER;
        } else if (Boolean.class.isAssignableFrom(fieldType) || fieldType == boolean.class) {
            return JsonFieldType.BOOLEAN;
        } else if (List.class.isAssignableFrom(fieldType)) {
            return JsonFieldType.ARRAY;
        } else {
            return JsonFieldType.OBJECT;
        }
    }


}
