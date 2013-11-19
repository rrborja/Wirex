/*
 * The MIT License
 *
 * Copyright 2013 Ritchie Borja.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package net.wirex.structures;

import ca.odell.glazedlists.TreeList;
import com.google.common.collect.Lists;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author Ritchie Borja
 */
public class XTreeFormat implements TreeList.Format {

    private Object model;
    private Class modelClass;

    public XTreeFormat(Object model) {
        this.model = model;
        this.modelClass = model.getClass();
    }

    @Override
    public void getPath(List path, Object element) {
        XNode node = (XNode) element;
        path.addAll(Lists.newArrayList(node.getPath()));
        path.add(node);
    }

    @Override
    public boolean allowsChildren(Object element) {
        return true;
    }

    @Override
    public Comparator getComparator(int depth) {
        return null;
    }
}
