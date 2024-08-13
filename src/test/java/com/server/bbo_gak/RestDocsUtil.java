package com.server.bbo_gak;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.PayloadDocumentation;

public class RestDocsUtil {

    public static List<FieldDescriptor> extractFieldDescriptors(Class<?> dtoClass) {
        return extractFieldDescriptors("", dtoClass);
    }

    private static List<FieldDescriptor> extractFieldDescriptors(String pathPrefix, Class<?> dtoClass) {
        List<FieldDescriptor> fieldDescriptors = new ArrayList<>();

        for (Field field : dtoClass.getDeclaredFields()) {
            String fullPath = pathPrefix.isEmpty() ? field.getName() : pathPrefix + "." + field.getName();
            if (isList(field)) {
                Type genericType = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
                if (genericType instanceof Class) {
                    // Handle lists in request or response
                    fieldDescriptors.add(PayloadDocumentation.fieldWithPath(fullPath + "[]")
                        .description("List of " + ((Class<?>) genericType).getSimpleName()));
                    fieldDescriptors.addAll(extractFieldDescriptors(fullPath + "[].", (Class<?>) genericType));
                }
            } else if (isSimpleType(field)) {
                fieldDescriptors.add(PayloadDocumentation.fieldWithPath(fullPath)
                    .description(generateDescription(field)));
            } else {
                fieldDescriptors.addAll(extractFieldDescriptors(fullPath, field.getType()));
            }
        }

        return fieldDescriptors;
    }

    private static boolean isList(Field field) {
        return List.class.isAssignableFrom(field.getType());
    }

    private static boolean isSimpleType(Field field) {
        Class<?> type = field.getType();
        return type.isPrimitive() || type == String.class || type == Integer.class || type == Long.class ||
            type == Boolean.class || type == Double.class || type == Float.class || type == Byte.class ||
            type == Short.class || type == Character.class;
    }

    private static String generateDescription(Field field) {
        // Implement a way to generate or fetch descriptions for fields
        // For simplicity, we'll just use the field name as description here
        return "Description for " + field.getName();
    }
}
