/*
 * TableIndex.java
 * Transform
 *
 * Copyright (c) 2001-2009 Flagstone Software Ltd. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *  * Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *  * Neither the name of Flagstone Software Ltd. nor the names of its
 *    contributors may be used to endorse or promote products derived from this
 *    software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package com.flagstone.transform.action;


import com.flagstone.transform.exception.IllegalArgumentRangeException;

/**
 * TableIndex is used with an Push action to push a reference to an entry in a
 * Table of string literals onto the stack.
 *
 * @see Table
 * @see Push
 */
public final class TableIndex {

    private static final String FORMAT = "TableIndex: { index=%d }";

    private final transient int index;

    /**
     * Creates a TableIndex object referencing the value stored in the literal
     * table.
     *
     * @param anIndex
     *            the index into the literal table. Must be in the range
     *            0..65535.
     */
    public TableIndex(final int anIndex) {
        if ((anIndex < 0) || (anIndex > 65535)) {
            throw new IllegalArgumentRangeException(0, 65535, anIndex);
        }
        index = anIndex;
    }

    /**
     * Returns the index in the table of string literals.
     */
    public int getIndex() {
        return index;
    }

    /** {@inheritDoc} */
   @Override
    public String toString() {
        return String.format(FORMAT, index);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object other) {
        boolean result;

        if (other == null) {
            result = false;
        } else if (other == this) {
            result = true;
        } else if (other instanceof TableIndex) {
            result = index == ((TableIndex) other).index;
        } else {
            result = false;
        }
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return 31 * index;
    }
}
