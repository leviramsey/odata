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
package com.sdl.odata.api;

/**
 * Superclass for exceptions that occur because there is something wrong with a request.
 * <p>
 * This will normally lead to a response being sent back to the client with a 4xx status code (client error).
 */
public class ODataClientException extends ODataException {

    public ODataClientException(ODataErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public ODataClientException(ODataErrorCode errorCode, String message, String target) {
        super(errorCode, message, target);
    }

    public ODataClientException(ODataErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }

    public ODataClientException(ODataErrorCode errorCode, String message, String target, Throwable cause) {
        super(errorCode, message, target, cause);
    }
}
