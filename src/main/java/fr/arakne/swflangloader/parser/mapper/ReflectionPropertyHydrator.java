package fr.arakne.swflangloader.parser.mapper;

import java.lang.reflect.Field;

/**
 * Hydrator using reflection field
 */
final public class ReflectionPropertyHydrator<T, V> implements PropertyHydrator<T, V> {
    final private Field field;

    public ReflectionPropertyHydrator(Field field) {
        this.field = field;

        field.setAccessible(true);
    }

    @Override
    @SuppressWarnings("unchecked")
    public V get(T source) {
        try {
            return (V) field.get(source);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e); // Should not occurs
        }
    }

    @Override
    public void set(T target, V value) {
        try {
            field.set(target, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e); // Should not occurs
        }
    }
}
