package fr.arakne.swflangloader.parser.mapper;

/**
 *
 * @param <T>
 * @param <V>
 */
interface PropertyHydrator<T, V> {
    public V get(T source);

    public void set(T target, V value);
}
