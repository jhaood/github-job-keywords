package com.aestheticsw.jobkeywords.client.rest;

import java.io.IOException;
import java.util.Collection;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.deser.std.CollectionDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;

public class XmlWhitespaceModule extends SimpleModule {

    private static final long serialVersionUID = 1L;

    public XmlWhitespaceModule() {
        super(XmlWhitespaceModule.class.getSimpleName());
    }

    @Override
    public void setupModule(SetupContext context) {
        super.setupModule(context);
        context.addBeanDeserializerModifier(new BeanDeserializerModifier() {
            @Override
            public JsonDeserializer<?> modifyCollectionDeserializer(
                    DeserializationConfig config, CollectionType type,
                    BeanDescription beanDesc, JsonDeserializer<?> deserializer) {
                if (deserializer instanceof CollectionDeserializer) {
                    return new CustomizedCollectionDeserialiser(
                            (CollectionDeserializer) deserializer);
                } else {
                    return super.modifyCollectionDeserializer(config, type, beanDesc,
                            deserializer);
                }
            }
        });
    }

    private static class CustomizedCollectionDeserialiser extends CollectionDeserializer {

        public CustomizedCollectionDeserialiser(CollectionDeserializer src) {
            super(src);
        }

        private static final long serialVersionUID = 1L;

        @SuppressWarnings("unchecked")
        @Override
        public Collection<Object> deserialize(JsonParser jp, DeserializationContext ctxt)
                throws IOException, JsonProcessingException {
            if (jp.getCurrentToken() == JsonToken.VALUE_STRING
                    && jp.getText().matches("^[\\r\\n\\t ]+$")) {
                return (Collection<Object>) _valueInstantiator.createUsingDefault(ctxt);
            }
            return super.deserialize(jp, ctxt);
        }

        @Override
        public CollectionDeserializer createContextual(DeserializationContext ctxt,
                BeanProperty property) throws JsonMappingException {
            return new CustomizedCollectionDeserialiser(super.createContextual(ctxt, property));
        }

    }
}