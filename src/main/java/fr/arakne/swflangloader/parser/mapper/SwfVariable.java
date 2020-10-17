package fr.arakne.swflangloader.parser.mapper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Define the field as swf variable
 * The field type will be used as assignation type
 * To define a custom swf variable name, use the annotation value. By default, use the field name
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SwfVariable {
    /**
     * @return The swf variable name
     */
    public String value() default "";
}
