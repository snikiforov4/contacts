package contacts.service;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class ClassFieldSetter {

    private final Map<String, Method> methodsForEdit;
    private final List<Field> searchableFields;

    private ClassFieldSetter(Map<String, Method> methodsForEdit, List<Field> searchableFields) {
        this.methodsForEdit = Map.copyOf(methodsForEdit);
        this.searchableFields = List.copyOf(searchableFields);
    }

    public static ClassFieldSetter forClass(Class<?> aClass) {
        Objects.requireNonNull(aClass);
        List<Class<?>> allClassesChain = getAllClassesChain(aClass);
        Map<String, Method> editable = new HashMap<>();
        for (Class<?> cls : allClassesChain) {
            editable.putAll(collectEditableFieldsMethods(cls));
        }
        List<Field> searchable = new ArrayList<>();
        for (Class<?> cls : allClassesChain) {
            searchable.addAll(collectSearchableFields(cls));
        }
        return new ClassFieldSetter(editable, searchable);
    }

    private static List<Class<?>> getAllClassesChain(Class<?> aClass) {
        List<Class<?>> result = new ArrayList<>();
        Class<?> analyzedClass = aClass;
        do {
            result.add(analyzedClass);
            analyzedClass = analyzedClass.getSuperclass();
        } while (analyzedClass != null && analyzedClass != Object.class);
        Collections.reverse(result);
        return result;
    }

    private static Map<String, Method> collectEditableFieldsMethods(Class<?> aClass) {
        Map<String, Method> result = new HashMap<>();
        for (Field field : aClass.getDeclaredFields()) {
            EditableField editableAnnotation = field.getAnnotation(EditableField.class);
            if (editableAnnotation != null) {
                String fieldName = getFieldName(field, editableAnnotation);
                String methodName = getMethodName(field);
                try {
                    Method method = aClass.getDeclaredMethod(methodName, String.class);
                    result.put(fieldName, method);
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException("Appropriate setter method does not exist for field=" + fieldName);
                }
            }
        }
        return result;
    }

    private static String getMethodName(Field field) {
        String fieldName = field.getName();
        return "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }

    private static String getFieldName(Field field, EditableField editableAnnotation) {
        return editableAnnotation.name().isEmpty() ? field.getName() : editableAnnotation.name();
    }

    private static List<Field> collectSearchableFields(Class<?> aClass) {
        List<Field> result = new ArrayList<>();
        for (Field field : aClass.getDeclaredFields()) {
            SearchableField editableAnnotation = field.getAnnotation(SearchableField.class);
            if (editableAnnotation != null) {
                result.add(field);
            }
        }
        return result;
    }

    public Set<String> getAllEditableFieldNames() {
        return methodsForEdit.keySet();
    }

    public List<Field> getSearchableFields() {
        return searchableFields;
    }

    public boolean updateField(Object instance, String fieldName, String value) {
        Objects.requireNonNull(instance);
        Method method = methodsForEdit.get(fieldName);
        if (method == null) {
            return false;
        }
        try {
            method.invoke(instance, value);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

}
