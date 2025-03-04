/*
 * Copyright (c) 2014-2021 All Rights Reserved by the RWS Group for and on behalf of its affiliates and subsidiaries.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sdl.odata.edm.model;

import com.sdl.odata.api.edm.model.StructuralProperty;
import com.sdl.odata.api.edm.model.Type;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;

import static com.sdl.odata.util.ReferenceUtil.isNullOrEmpty;

/**
 * Implementation of common functionality for {@code PropertyImpl} and {@code NavigationPropertyImpl}.
 *
 */
public abstract class StructuralPropertyImpl implements StructuralProperty {
    /**
     * Structural Property Builder.
     *
     * @param <B> builder
     */
    public abstract static class Builder<B extends Builder> {
        /**
         * Begin Index.
         */
        public static final int BEGIN_INDEX = 11;
        private final B self;

        private String name;
        private String typeName;
        private boolean isCollection;
        private boolean isNullable = true;
        private Field javaField;

        protected Builder() {
            this.self = (B) this;
        }

        public B setName(String builderName) {
            this.name = builderName;
            return self;
        }

        public B setTypeName(String builderTypeName) {
            if (builderTypeName.startsWith("Collection(") && builderTypeName.endsWith(")")) {
                this.typeName = builderTypeName.substring(BEGIN_INDEX, builderTypeName.length() - 1);
                isCollection = true;
            } else {
                this.typeName = builderTypeName;
                isCollection = false;
            }
            return self;
        }

        public B setType(Type type, boolean isCollectionForStructureProperty) {
            this.typeName = type.getFullyQualifiedName();
            this.isCollection = isCollectionForStructureProperty;
            return self;
        }

        public B setTypeFromJavaField(Field field, TypeNameResolver resolver) {
            // Find out if the field is an array or collection; get the element type if that is the case
            Class<?> cls = field.getType();
            if (cls.isArray()) {
                this.isCollection = true;
                cls = cls.getComponentType();
            } else if (Collection.class.isAssignableFrom(cls)) {
                this.isCollection = true;
                cls = getCollectionElementType(field);
            } else {
                this.isCollection = false;
            }

            this.typeName = resolver.resolveTypeName(cls);
            if (isNullOrEmpty(this.typeName)) {
                throw new IllegalArgumentException("The OData type of this field cannot be determined from " +
                        "the Java type: " + field.toGenericString());
            }

            return self;
        }

        private Class<?> getCollectionElementType(Field field) {
            // Reflection magic to determine the element type of a collection type
            java.lang.reflect.Type genericType = field.getGenericType();
            if (genericType instanceof ParameterizedType) {
                java.lang.reflect.Type[] actualTypeArguments =
                        ((ParameterizedType) genericType).getActualTypeArguments();
                if (actualTypeArguments.length > 0 && actualTypeArguments[0] instanceof Class) {
                    return (Class<?>) actualTypeArguments[0];
                }
            }

            throw new IllegalArgumentException("The element type of this collection type cannot be determined: "
                    + genericType);
        }

        public B setIsNullable(boolean isNull) {
            this.isNullable = isNull;
            return self;
        }

        public B setJavaField(Field field) {
            this.javaField = field;
            return self;
        }
    }

    private final String name;
    private final String typeName;
    private final boolean isCollection;
    private final boolean isNullable;
    private final Field javaField;

    protected StructuralPropertyImpl(Builder builder) {
        this.name = builder.name;
        this.typeName = builder.typeName;
        this.isCollection = builder.isCollection;
        this.isNullable = builder.isNullable;
        this.javaField = builder.javaField;
    }

    public String getName() {
        return name;
    }

    public String getTypeName() {
        return isCollection() ? "Collection(" + typeName + ")" : typeName;
    }

    public String getElementTypeName() {
        return isCollection() ? typeName : null;
    }

    public boolean isCollection() {
        return isCollection;
    }

    public boolean isNullable() {
        return isNullable;
    }

    public Field getJavaField() {
        return javaField;
    }

    @Override
    public String toString() {
        return name;
    }
}
