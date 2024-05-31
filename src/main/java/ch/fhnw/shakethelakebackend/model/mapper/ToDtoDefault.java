package ch.fhnw.shakethelakebackend.model.mapper;

import org.mapstruct.Named;
import org.mapstruct.Qualifier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Qualifier // make sure that this is the MapStruct qualifier annotation
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
@Named("ToDtoDefault")
public @interface ToDtoDefault {
}
