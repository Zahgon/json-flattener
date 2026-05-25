/*
 *
 * Copyright 2016 Wei-Ming Wu
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
package com.github.wnameless.json.flattener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import org.apache.commons.text.translate.CharSequenceTranslator;
import com.github.wnameless.json.base.JsonPrinter;

/**
 * {@link JsonifyArrayList} is simply a ArrayList but with an override jsonify toString method.
 *
 * @author Wei-Ming Wu
 *
 * @param <E> the type of elements
 */
public class JsonifyArrayList<E> extends ArrayList<E> {

    private static final long serialVersionUID = 1L;

    private CharSequenceTranslator translator = StringEscapePolicy.DEFAULT.getCharSequenceTranslator();

    public JsonifyArrayList() {
    }

    public JsonifyArrayList(Collection<E> coll) {
        super(coll);
    }

    public void setTranslator(CharSequenceTranslator translator) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    public String toString(PrintMode printMode) {
        throw new UnsupportedOperationException("STUB: not implemented");
    }

    @Override
    public String toString() {
        throw new UnsupportedOperationException("STUB: not implemented");
    }
}
