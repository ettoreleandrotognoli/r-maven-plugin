package io.github.ettoreleandrotognoli.r;

import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import io.github.ettoreleandrotognoli.r.domain.resource.ResourceDirectory;
import io.github.ettoreleandrotognoli.r.domain.resource.ResourceFile;
import io.github.ettoreleandrotognoli.r.domain.resource.ResourceNode;
import io.github.ettoreleandrotognoli.r.domain.resource.ResourceNodeProcessor;

import javax.lang.model.element.Modifier;
import java.io.File;

public class RClassBuilder implements ResourceNodeProcessor {


    protected TypeSpec.Builder typeSpecBuilder;

    public RClassBuilder() {
        typeSpecBuilder = TypeSpec.enumBuilder("R")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC);
        FieldSpec pathField = FieldSpec.builder(String.class, "path").build();
        typeSpecBuilder.addField(pathField);
        MethodSpec constructorMethod = MethodSpec.constructorBuilder()
                .addParameter(String.class, "path")
                .addCode("this.path = path;")
                .build();
        MethodSpec getPathMethod = MethodSpec.methodBuilder("getPath")
                .addModifiers(Modifier.PUBLIC)
                .returns(String.class)
                .addCode("return path;")
                .build();
        typeSpecBuilder.addMethod(constructorMethod);
        typeSpecBuilder.addMethod(getPathMethod);
    }

    public RClassBuilder(TypeSpec.Builder typeSpecBuilder) {
        this.typeSpecBuilder = typeSpecBuilder;
    }

    @Override
    public void process(ResourceDirectory resourceDirectory) {

    }

    @Override
    public void process(ResourceFile resourceFile) {
        String resourcePath = resourceFile.getPath();
        String fieldName = resourcePath
                .replaceAll("[^a-z0-9_]","_")
                .toUpperCase();
        TypeSpec enumItem = TypeSpec.anonymousClassBuilder("$S", resourcePath).build();
        this.typeSpecBuilder.addEnumConstant(fieldName, enumItem);
    }


    public TypeSpec build() {
        return typeSpecBuilder.build();
    }
}
