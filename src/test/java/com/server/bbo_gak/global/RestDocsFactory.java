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
import org.springframework.test.web.servlet.RequestBuilder;

@Component
public class RestDocsFactory {

    public RequestBuilder createRequest(String url, Object requestDto, HttpMethod method, ObjectMapper objectMapper
    ) throws Exception {
        String content = objectMapper.writeValueAsString(requestDto);

        switch (method.name()) {
            case "POST":
                return RestDocumentationRequestBuilders.post(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content)
                    .accept(MediaType.APPLICATION_JSON);
            case "GET":
                return RestDocumentationRequestBuilders.get(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content)
                    .accept(MediaType.APPLICATION_JSON);
            case "PUT":
                return RestDocumentationRequestBuilders.put(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content)
                    .accept(MediaType.APPLICATION_JSON);
            case "DELETE":
                return RestDocumentationRequestBuilders.delete(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content)
                    .accept(MediaType.APPLICATION_JSON);
            default:
                throw new IllegalArgumentException("Invalid HTTP method: " + method);
        }
    }

    public <T, R> RestDocumentationResultHandler getSuccessResource(String identifier, String tag,
        T requestDto, R responseDto) {

        String requestSchemaName = requestDto.getClass().getSimpleName();
        String responseSchemaName = responseDto.getClass().getSimpleName();
        return MockMvcRestDocumentationWrapper.document(identifier,
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            resource(
                ResourceSnippetParameters.builder()
                    .tags(tag)
                    .requestSchema(Schema.schema(requestSchemaName))
                    .responseSchema(Schema.schema(responseSchemaName))
                    .requestFields(getRequestFields(requestDto))
                    .responseFields(getResponseFields(responseDto))
                    .build()
            )
        );
    }

    private <T> FieldDescriptor[] getRequestFields(T requestDto) {
        List<FieldDescriptor> fields = new ArrayList<>();
        Field[] declaredFields = requestDto.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            String name = field.getName();
            JsonFieldType type = determineFieldType(field.getType());
            Object value = null;

            try {
                value = field.get(requestDto);
            } catch (IllegalAccessException e) {
                e.printStackTrace(); // 필요에 따라 예외 처리
            }

            fields.add(PayloadDocumentation.fieldWithPath(name)
                .type(type)
                .description(name + " field")
                .attributes(Attributes.key("example").value(value != null ? value.toString() : "null"))); // 필드 값 추가
        }

        return fields.toArray(new FieldDescriptor[0]);
    }

    private <T> FieldDescriptor[] getResponseFields(T responseDto) {
        List<FieldDescriptor> fields = new ArrayList<>();
        Field[] declaredFields = responseDto.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            String name = field.getName();
            JsonFieldType type = determineFieldType(field.getType());
            Object value = null;

            try {
                value = field.get(responseDto); // 필드 값 가져오기
            } catch (IllegalAccessException e) {
                e.printStackTrace(); // 필요에 따라 예외 처리
            }

            fields.add(PayloadDocumentation.fieldWithPath(name)
                .type(type)
                .description(name + " field")
                .attributes(Attributes.key("example").value(value != null ? value.toString() : "null"))); // 필드 값 추가
        }

        return fields.toArray(new FieldDescriptor[0]);
    }

    private JsonFieldType determineFieldType(Class<?> clazz) {
        if (clazz == String.class) {
            return JsonFieldType.STRING;
        } else if (clazz == Integer.class || clazz == int.class) {
            return JsonFieldType.NUMBER;
        } else if (clazz == Boolean.class || clazz == boolean.class) {
            return JsonFieldType.BOOLEAN;
        } else if (clazz == Long.class || clazz == long.class) {
            return JsonFieldType.NUMBER;
        } else {
            return JsonFieldType.OBJECT; // 기본적으로 객체로 처리
        }
    }


}
