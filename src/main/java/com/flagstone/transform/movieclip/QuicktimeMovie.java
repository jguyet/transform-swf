/*
 * QuicktimeMovie.java
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

package com.flagstone.transform.movieclip;


import com.flagstone.transform.coder.CoderException;
import com.flagstone.transform.coder.Context;
import com.flagstone.transform.coder.MovieTag;
import com.flagstone.transform.coder.MovieTypes;
import com.flagstone.transform.coder.SWFDecoder;
import com.flagstone.transform.coder.SWFEncoder;

/**
 * The QuicktimeMovie defines the path to an Quicktime movie to be played.
 */
//TODO(class)
public final class QuicktimeMovie implements MovieTag {
    private static final String FORMAT = "QuicktimeMovie: { name=%s }";

    private String path;

    private transient int length;

    /**
     * Creates and initialises a QuicktimeMovie object using values encoded
     * in the Flash binary format.
     *
     * @param coder
     *            an SWFDecoder object that contains the encoded Flash data.
     *
     * @throws CoderException
     *             if an error occurs while decoding the data.
     */
    public QuicktimeMovie(final SWFDecoder coder) throws CoderException {
        final int start = coder.getPointer();
        length = coder.readWord(2, false) & 0x3F;

        if (length == 0x3F) {
            length = coder.readWord(4, false);
        }
        final int end = coder.getPointer() + (length << 3);

        path = coder.readString();

        if (coder.getPointer() != end) {
            throw new CoderException(getClass().getName(), start >> 3, length,
                    (coder.getPointer() - end) >> 3);
        }
    }

    /**
     * Creates a QuicktimeMovie object referencing the specified file.
     *
     * @param aString
     *            the file or URL where the file is located. Must not be null.
     */
    public QuicktimeMovie(final String aString) {
        setPath(aString);
    }

    /**
     * Creates and initialises a QuicktimeMovie object using the values copied
     * from another QuicktimeMovie object.
     *
     * @param object
     *            a QuicktimeMovie object from which the values will be
     *            copied.
     */
    public QuicktimeMovie(final QuicktimeMovie object) {
        path = object.path;
    }

    /**
     * Returns the reference to the file containing the movie.
     */
    public String getPath() {
        return path;
    }

    /**
     * Sets the reference to the file containing the movie.
     *
     * @param aString
     *            the file or URL where the file is located. Must not be null.
     */
    public void setPath(final String aString) {
        if (aString == null) {
            throw new NullPointerException();
        }
        path = aString;
    }

    /** TODO(method). */
    public QuicktimeMovie copy() {
        return new QuicktimeMovie(this);
    }

    @Override
    public String toString() {
        return String.format(FORMAT, path);
    }

    /** {@inheritDoc} */
    public int prepareToEncode(final SWFEncoder coder, final Context context) {
        length = coder.strlen(path);

        return (length > 62 ? 6 : 2) + length;
    }

    /** {@inheritDoc} */
    public void encode(final SWFEncoder coder, final Context context)
            throws CoderException {
        final int start = coder.getPointer();

        if (length > 62) {
            coder.writeWord((MovieTypes.QUICKTIME_MOVIE << 6) | 0x3F, 2);
            coder.writeWord(length, 4);
        } else {
            coder.writeWord((MovieTypes.QUICKTIME_MOVIE << 6) | length, 2);
        }
        final int end = coder.getPointer() + (length << 3);

        coder.writeString(path);

        if (coder.getPointer() != end) {
            throw new CoderException(getClass().getName(), start >> 3, length,
                    (coder.getPointer() - end) >> 3);
        }
    }
}
