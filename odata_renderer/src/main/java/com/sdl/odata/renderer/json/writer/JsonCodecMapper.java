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
package com.sdl.odata.renderer.json.writer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.UUID;

/**
 * This mapper is extension of {@link ObjectMapper}.
 * Default object mapper doesn't contain for every possible type.
 *
 */
public class JsonCodecMapper extends ObjectMapper {
    @Override
    public void writeValue(final JsonGenerator jsonGenerator, final Object value) throws IOException {
        super.writeValue(jsonGenerator, processData(value));
    }

    private Object processData(Object rawValue) {
        if (rawValue instanceof UUID) {
            return rawValue.toString();
        } else {
            return rawValue;
        }
    }
}
