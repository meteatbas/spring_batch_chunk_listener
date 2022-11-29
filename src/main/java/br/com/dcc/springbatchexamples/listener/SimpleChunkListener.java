package br.com.dcc.springbatchexamples.listener;

import org.springframework.batch.core.annotation.AfterChunk;
import org.springframework.batch.core.annotation.BeforeChunk;
import org.springframework.batch.core.scope.context.ChunkContext;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SimpleChunkListener {

	@BeforeChunk
	public void SimpleChunkBeforeChunk(ChunkContext context) {

		System.out.print("before");
	}

	@AfterChunk
	public void SimpleChunkAfterChunk(ChunkContext context)  {
		System.out.print("after");
	}

}
