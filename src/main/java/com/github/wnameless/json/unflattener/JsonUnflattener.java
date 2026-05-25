/*
 *
 * Copyright 2015 Wei-Ming Wu
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 *
 */
package com.github.wnameless.json.unflattener;

import static com.github.wnameless.json.flattener.FlattenMode.MONGODB;
import static org.apache.commons.lang3.Validate.isTrue;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.github.wnameless.json.base.Jackson3JsonCore;
import com.github.wnameless.json.base.JsonArrayCore;
import com.github.wnameless.json.base.JsonCore;
import com.github.wnameless.json.base.JsonObjectCore;
import com.github.wnameless.json.base.JsonPrinter;
import com.github.wnameless.json.base.JsonValueBase;
import com.github.wnameless.json.base.JsonValueCore;
import com.github.wnameless.json.flattener.FlattenMode;
import com.github.wnameless.json.flattener.JsonifyLinkedHashMap;
import com.github.wnameless.json.flattener.KeyTransformer;
import com.github.wnameless.json.flattener.PrintMode;

/**
 * {@link JsonUnflattener} provides a static {@link #unflatten(String)} method to unflatten any
 * flattened JSON string back to nested one.
 *
 * @author Wei-Ming Wu
 */
public final class JsonUnflattener {

    /**
     * {@link ROOT} is the default key of the Map returned by {@link #unflattenAsMap}. When
     * {@link JsonUnflattener} processes a JSON string which is not a JSON object or array, the final
     * outcome may not suit in a Java Map. At that moment, {@link JsonUnflattener} will put the result
     * in the Map with {@link ROOT} as its key.
     */
    public static final String ROOT = "root";

    private static final Pattern naturalNumberPattern = Pattern.compile("\\d+");

    private static final Pattern illegalSeparatorPattern = Pattern.compile("[\"\\s]");

    private final Map<String, Pattern> patternCache = new HashMap<>();

    /**
     * Returns a JSON string of nested objects by the given flattened JSON string.
     *
     * @param json a flattened JSON string
     * @return a JSON string of nested objects
     */
    public static String unflatten(String json) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Returns a JSON string of nested objects by the given flattened Map.
     *
     * @param flattenedMap a flattened Map
     * @return a JSON string of nested objects
     */
    public static String unflatten(Map<String, ?> flattenedMap) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Returns a Java Map of nested objects by the given flattened JSON string.
     *
     * @param json a flattened JSON string
     * @return a Java Map of nested objects
     */
    public static Map<String, Object> unflattenAsMap(String json) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Returns a Java Map of nested objects by the given flattened Map.
     *
     * @param flattenedMap a flattened Map
     * @return a Java Map of nested objects
     */
    public static Map<String, Object> unflattenAsMap(Map<String, ?> flattenedMap) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    private final JsonCore<?> jsonCore;

    private final JsonValueCore<?> root;

    private FlattenMode flattenMode = FlattenMode.NORMAL;

    private Character separator = '.';

    private Character leftBracket = '[';

    private Character rightBracket = ']';

    private PrintMode printMode = PrintMode.MINIMAL;

    private KeyTransformer keyTrans = null;

    private JsonUnflattener newJsonUnflattener(JsonValueCore<?> jsonValue) {
        JsonUnflattener ju = new JsonUnflattener(jsonValue);
        ju.withFlattenMode(flattenMode);
        ju.withSeparator(separator);
        ju.withLeftAndRightBrackets(leftBracket, rightBracket);
        ju.withPrintMode(printMode);
        ju.withKeyTransformer(keyTrans);
        return ju;
    }

    private JsonUnflattener(JsonValueCore<?> root) {
        jsonCore = new Jackson3JsonCore();
        this.root = root;
    }

    private JsonValueCore<?> parseJson(String json) {
        return jsonCore.parse(json);
    }

    /**
     * Creates a JSON unflattener by given JSON string.
     *
     * @param json a JSON string
     */
    public JsonUnflattener(String json) {
        jsonCore = new Jackson3JsonCore();
        root = parseJson(json);
    }

    /**
     * Creates a JSON unflattener by given {@link JsonCore} and JSON string.
     *
     * @param jsonCore a {@link JsonCore}
     * @param json a JSON string
     */
    public JsonUnflattener(JsonCore<?> jsonCore, String json) {
        if (jsonCore == null)
            throw new NullPointerException();
        this.jsonCore = jsonCore;
        root = parseJson(json);
    }

    /**
     * Creates a JSON unflattener by given JSON string reader.
     *
     * @param jsonReader a JSON reader
     * @throws IOException if the jsonReader cannot be read
     */
    public JsonUnflattener(Reader jsonReader) throws IOException {
        jsonCore = new Jackson3JsonCore();
        root = jsonCore.parse(jsonReader);
    }

    /**
     * Creates a JSON unflattener by given {@link JsonCore} and JSON string reader.
     *
     * @param jsonCore a {@link JsonCore}
     * @param jsonReader a JSON reader
     * @throws IOException if the jsonReader cannot be read
     */
    public JsonUnflattener(JsonCore<?> jsonCore, Reader jsonReader) throws IOException {
        if (jsonCore == null)
            throw new NullPointerException();
        this.jsonCore = jsonCore;
        root = jsonCore.parse(jsonReader);
    }

    /**
     * Creates a JSON unflattener by given flattened {@link Map}.
     *
     * @param flattenedMap a flattened {@link Map}
     */
    public JsonUnflattener(Map<String, ?> flattenedMap) {
        jsonCore = new Jackson3JsonCore();
        root = jsonCore.parse(new JsonifyLinkedHashMap<>(flattenedMap).toString());
    }

    /**
     * Creates a JSON unflattener by given {@link JsonCore} and flattened {@link Map}.
     *
     * @param jsonCore a {@link JsonCore}
     * @param flattenedMap a flattened {@link Map}
     */
    public JsonUnflattener(JsonCore<?> jsonCore, Map<String, ?> flattenedMap) {
        if (jsonCore == null)
            throw new NullPointerException();
        this.jsonCore = jsonCore;
        root = jsonCore.parse(new JsonifyLinkedHashMap<>(flattenedMap).toString());
    }

    private Pattern arrayIndexPattern() {
        String regex = Pattern.quote(leftBracket.toString()) + "\\s*\\d+\\s*" + Pattern.quote(rightBracket.toString());
        if (!patternCache.containsKey(regex)) {
            patternCache.put(regex, Pattern.compile(regex));
        }
        return patternCache.get(regex);
    }

    private Pattern objectComplexKeyPattern() {
        String regex = Pattern.quote(leftBracket.toString()) + "\\s*\".*?\"\\s*" + Pattern.quote(rightBracket.toString());
        if (!patternCache.containsKey(regex)) {
            patternCache.put(regex, Pattern.compile(regex));
        }
        return patternCache.get(regex);
    }

    private Pattern objectKeyPattern() {
        String regex = "[^" + Pattern.quote(separator.toString()) + Pattern.quote(leftBracket.toString()) + Pattern.quote(rightBracket.toString()) + "]+";
        if (!patternCache.containsKey(regex)) {
            patternCache.put(regex, Pattern.compile(regex));
        }
        return patternCache.get(regex);
    }

    private Pattern keyPartPattern() {
        if (flattenMode.equals(MONGODB)) {
            // Escape the separator character for regex patterns
            String separatorRegex = Pattern.quote(separator.toString());
            // Escape the separator character for character classes
            String separatorCharClass = "\\^-$[]".contains(separator.toString()) ? "\\" + separator : separator.toString();
            // Construct the regex pattern
            String regex = // Words not containing the separator
            "\\b[^" + separatorCharClass + "\\s]+\\b" + "|^(?=" + separatorRegex + // Empty string before separator at start
            ")" + "|(?<=" + separatorRegex + // Empty string after separator at end
            ")$" + "|(?<=" + separatorRegex + ")(?=" + separatorRegex + // Empty strings between
            ")";
            // separators
            if (!patternCache.containsKey(regex)) {
                patternCache.put(regex, Pattern.compile(regex));
            }
            return patternCache.get(regex);
        } else {
            String regex = arrayIndexPattern().pattern() + "|" + objectComplexKeyPattern().pattern() + "|" + objectKeyPattern().pattern();
            if (!patternCache.containsKey(regex)) {
                patternCache.put(regex, Pattern.compile(regex));
            }
            return patternCache.get(regex);
        }
    }

    /**
     * A fluent setter to setup a mode of the {@link JsonUnflattener}.
     *
     * @param flattenMode a {@link FlattenMode}
     * @return this {@link JsonUnflattener}
     */
    public JsonUnflattener withFlattenMode(FlattenMode flattenMode) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * A fluent setter to setup the separator within a key in the flattened JSON. The default
     * separator is a dot(.).
     *
     * @param separator any character
     * @return this {@link JsonUnflattener}
     */
    public JsonUnflattener withSeparator(char separator) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    private Pattern illegalBracketsPattern() {
        return Pattern.compile("[\"\\s" + Pattern.quote(separator.toString()) + "]");
    }

    /**
     * A fluent setter to setup the left and right brackets within a key in the flattened JSON. The
     * default left and right brackets are left square bracket([) and right square bracket(]).
     *
     * @param leftBracket any character
     * @param rightBracket any character
     * @return this {@link JsonUnflattener}
     */
    public JsonUnflattener withLeftAndRightBrackets(char leftBracket, char rightBracket) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * A fluent setter to setup a print mode of the {@link JsonUnflattener}. The default print mode is
     * minimal.
     *
     * @param printMode a {@link PrintMode}
     * @return this {@link JsonUnflattener}
     */
    public JsonUnflattener withPrintMode(PrintMode printMode) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * A fluent setter to setup a {@link KeyTransformer} of the {@link JsonUnflattener}.
     *
     * @param keyTrans a {@link KeyTransformer}
     * @return this {@link JsonUnflattener}
     */
    public JsonUnflattener withKeyTransformer(KeyTransformer keyTrans) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    private String writeByConfig(JsonValueBase<?> jsonValue) {
        switch(printMode) {
            case PRETTY:
                return JsonPrinter.prettyPrint(jsonValue.toJson());
            default:
                return jsonValue.toJson();
        }
    }

    /**
     * Returns a JSON string of nested objects by the given flattened JSON string.
     *
     * @return a JSON string of nested objects
     */
    public String unflatten() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    /**
     * Returns a Java Map of nested objects by the given flattened JSON string.
     *
     * @return a Java Map of nested objects
     */
    public Map<String, Object> unflattenAsMap() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    private JsonArrayCore<?> unflattenArray(JsonArrayCore<?> array) {
        JsonArrayCore<?> unflattenArray = jsonCore.parse("[]").asArray();
        for (JsonValueCore<?> value : array) {
            if (value.isArray()) {
                unflattenArray.add(unflattenArray(value.asArray()));
            } else if (value.isObject()) {
                JsonValueCore<?> obj;
                obj = jsonCore.parse(newJsonUnflattener(value).unflatten());
                unflattenArray.add(obj);
            } else {
                unflattenArray.add(value);
            }
        }
        return unflattenArray;
    }

    private String extractKey(String keyPart) {
        if (objectComplexKeyPattern().matcher(keyPart).matches()) {
            keyPart = keyPart.replaceAll("^" + Pattern.quote(leftBracket.toString()) + "\\s*\"", "");
            keyPart = keyPart.replaceAll("\"\\s*" + Pattern.quote(rightBracket.toString()) + "$", "");
        }
        return keyTrans != null ? keyTrans.transform(keyPart) : keyPart;
    }

    private Integer extractIndex(String keyPart) {
        if (flattenMode.equals(MONGODB))
            return Integer.valueOf(keyPart);
        else
            return Integer.valueOf(keyPart.replaceAll("[" + Pattern.quote(leftBracket.toString()) + Pattern.quote(rightBracket.toString()) + "\\s]", ""));
    }

    private boolean isJsonArray(String keyPart) {
        return arrayIndexPattern().matcher(keyPart).matches() || (flattenMode.equals(MONGODB) && naturalNumberPattern.matcher(keyPart).matches());
    }

    private JsonArrayCore<?> findOrCreateJsonArray(JsonValueCore<?> currentVal, String objKey, Integer aryIdx) {
        if (objKey != null) {
            JsonObjectCore<?> jsonObj = currentVal.asObject();
            if (jsonObj.get(objKey) == null) {
                JsonArrayCore<?> ary = jsonCore.parse("[]").asArray();
                jsonObj.set(objKey, ary);
                return ary;
            }
            return jsonObj.get(objKey).asArray();
        } else {
            // aryIdx != null
            JsonArrayCore<?> jsonAry = currentVal.asArray();
            if (jsonAry.size() <= aryIdx || jsonAry.get(aryIdx).isNull()) {
                JsonArrayCore<?> ary = jsonCore.parse("[]").asArray();
                assureJsonArraySize(jsonAry, aryIdx);
                jsonAry.set(aryIdx, ary);
                return ary;
            }
            return jsonAry.get(aryIdx).asArray();
        }
    }

    private JsonObjectCore<?> findOrCreateJsonObject(JsonValueCore<?> currentVal, String objKey, Integer aryIdx) {
        if (objKey != null) {
            JsonObjectCore<?> jsonObj = currentVal.asObject();
            if (jsonObj.get(objKey) == null) {
                JsonObjectCore<?> obj = jsonCore.parse("{}").asObject();
                jsonObj.set(objKey, obj);
                return obj;
            }
            return jsonObj.get(objKey).asObject();
        } else {
            // aryIdx != null
            JsonArrayCore<?> jsonAry = currentVal.asArray();
            if (jsonAry.size() <= aryIdx || jsonAry.get(aryIdx).isNull()) {
                JsonObjectCore<?> obj = jsonCore.parse("{}").asObject();
                assureJsonArraySize(jsonAry, aryIdx);
                jsonAry.set(aryIdx, obj);
                return obj;
            }
            return jsonAry.get(aryIdx).asObject();
        }
    }

    private void setUnflattenedValue(JsonObjectCore<?> flattened, String key, JsonValueCore<?> currentVal, String objKey, Integer aryIdx) {
        JsonValueCore<?> val = flattened.get(key);
        if (objKey != null) {
            if (val.isArray()) {
                JsonArrayCore<?> jsonArray = jsonCore.parse("[]").asArray();
                for (JsonValueCore<?> arrayVal : val.asArray()) {
                    jsonArray.add(parseJson(newJsonUnflattener(arrayVal).unflatten()));
                }
                currentVal.asObject().set(objKey, jsonArray);
            } else {
                currentVal.asObject().set(objKey, val);
            }
        } else {
            // aryIdx != null
            assureJsonArraySize(currentVal.asArray(), aryIdx);
            currentVal.asArray().set(aryIdx, val);
        }
    }

    private void assureJsonArraySize(JsonArrayCore<?> jsonArray, Integer index) {
        while (index >= jsonArray.size()) {
            jsonArray.add(jsonCore.parse("null"));
        }
    }

    @Override
    public int hashCode() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    @Override
    public boolean equals(Object o) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    @Override
    public String toString() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }
}
