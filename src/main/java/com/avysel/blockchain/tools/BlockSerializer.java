package com.avysel.blockchain.tools;

import java.io.IOException;

import com.avysel.blockchain.model.block.Block;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class BlockSerializer extends StdSerializer<Block> {

	private static final long serialVersionUID = 1389379775308517009L;

	public BlockSerializer() {
		this(null);
	}
	
	protected BlockSerializer(Class<Block> t) {
		super(t);
	}

	@Override
	public void serialize(Block value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		
	}

}
