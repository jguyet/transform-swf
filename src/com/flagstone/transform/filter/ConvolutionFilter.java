package com.flagstone.transform.filter;

import com.flagstone.transform.coder.CoderException;
import com.flagstone.transform.coder.SWFContext;
import com.flagstone.transform.coder.SWFDecoder;
import com.flagstone.transform.coder.SWFEncoder;

public final class ConvolutionFilter implements Filter {

	public ConvolutionFilter(final SWFDecoder coder, final SWFContext context) throws CoderException {
		
	}
	
	public ConvolutionFilter(ConvolutionFilter object) {
		
	}
	
	public ConvolutionFilter copy() {
		return new ConvolutionFilter(this);	
	}

	public int prepareToEncode(final SWFEncoder coder, final SWFContext context)
	{
		return 0;
	}

	public void encode(final SWFEncoder coder, final SWFContext context) throws CoderException
	{
	}
}