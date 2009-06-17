/*
 * SolidFill.java
 * Transform
 *
 * Copyright (c) 2001-2009 Flagstone Software Ltd. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 *  * Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *  * Neither the name of Flagstone Software Ltd. nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.flagstone.transform.fillstyle;


import com.flagstone.transform.coder.CoderException;
import com.flagstone.transform.coder.Context;
import com.flagstone.transform.coder.FillStyle;
import com.flagstone.transform.coder.SWFDecoder;
import com.flagstone.transform.coder.SWFEncoder;
import com.flagstone.transform.datatype.Color;

/**
 * SolidFill defines a solid colour that is used to fill an enclosed area in a
 * shape. Shapes can be filled with transparent colours but only if the fill
 * style is used in a {@link DefineShape3} object.
 *
 * @see DefineShape
 * @see DefineShape2
 * @see DefineShape3
 */
//TODO(class)
public final class SolidFill implements FillStyle {

    private static final String FORMAT = "SolidFill: { color=%s }";

    private Color color;

    /**
     * Creates and initialises a SolidFill fill style using values encoded
     * in the Flash binary format.
     *
     * @param coder
     *            an SWFDecoder object that contains the encoded Flash data.
     *
     * @param context
     *            a Context object used to manage the decoders for different
     *            type of object and to pass information on how objects are
     *            decoded.
     *
     * @throws CoderException
     *             if an error occurs while decoding the data.
     */
    public SolidFill(final SWFDecoder coder, final Context context)
            throws CoderException {
        coder.adjustPointer(8);
        color = new Color(coder, context);
    }

    /**
     * Creates a SolidFill object with the specified colour.
     *
     * @param aColor
     *            an Color object that defines the colour that the area will be
     *            filled with. Must not be null.
     */
    public SolidFill(final Color aColor) {
        setColor(aColor);
    }

    /**
     * Creates and initialises a SolidFill fill style using the values copied
     * from another SolidFill object.
     *
     * @param object
     *            a SolidFill fill style from which the values will be
     *            copied.
     */
    public SolidFill(final SolidFill object) {
        color = object.color;
    }

    /**
     * Returns the colour of the fill style.
     */
    public Color getColor() {
        return color;
    }

    /**
     * Sets the colour of the fill style.
     *
     * @param aColor
     *            an Color object that defines the colour that the area will be
     *            filled with. Must not be null.
     */
    public void setColor(final Color aColor) {
        if (aColor == null) {
            throw new NullPointerException();
        }
        color = aColor;
    }

    /** TODO(method). */
    public SolidFill copy() {
        return new SolidFill(this);
    }

    @Override
    public String toString() {
        return String.format(FORMAT, color.toString());
    }

    /** {@inheritDoc} */
    public int prepareToEncode(final SWFEncoder coder, final Context context) {
        // TODO(optimise) calculate size of color directly.
        return 1 + color.prepareToEncode(coder, context);
    }

    /** {@inheritDoc} */
    public void encode(final SWFEncoder coder, final Context context)
            throws CoderException {
        coder.writeByte(0);
        color.encode(coder, context);
    }
}
