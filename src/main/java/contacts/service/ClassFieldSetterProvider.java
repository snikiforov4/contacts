package contacts.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClassFieldSetterProvider {

    private final Map<Class<?>, ClassFieldSetter> map = new ConcurrentHashMap<>();

    public ClassFieldSetter forClass(Class<?> aClass) {
        return map.computeIfAbsent(aClass, ClassFieldSetter::forClass);
    }

}
