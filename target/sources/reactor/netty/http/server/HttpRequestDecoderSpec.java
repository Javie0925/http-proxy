/*
 * Copyright (c) 2011-2019 Pivotal Software Inc, All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package reactor.netty.http.server;

import java.util.function.Function;

import reactor.netty.http.HttpDecoderSpec;
import reactor.netty.tcp.TcpServer;

/**
 * A configuration builder to fine tune the {@link io.netty.handler.codec.http.HttpServerCodec}
 * (or more precisely the {@link io.netty.handler.codec.http.HttpServerCodec.HttpServerRequestDecoder}).
 * <p>
 * Defaults are accessible as constants {@link #DEFAULT_MAX_INITIAL_LINE_LENGTH}, {@link #DEFAULT_MAX_HEADER_SIZE},
 * {@link #DEFAULT_MAX_CHUNK_SIZE}, {@link #DEFAULT_INITIAL_BUFFER_SIZE} and {@link #DEFAULT_VALIDATE_HEADERS}.
 *
 * @author Simon Baslé
 */
public final class HttpRequestDecoderSpec extends HttpDecoderSpec<HttpRequestDecoderSpec> {

	@Override
	public HttpRequestDecoderSpec maxInitialLineLength(int value) {
		return super.maxInitialLineLength(value);
	}

	@Override
	public HttpRequestDecoderSpec maxHeaderSize(int value) {
		return super.maxHeaderSize(value);
	}

	@Override
	public HttpRequestDecoderSpec maxChunkSize(int value) {
		return super.maxChunkSize(value);
	}

	@Override
	public HttpRequestDecoderSpec validateHeaders(boolean validate) {
		return super.validateHeaders(validate);
	}

	@Override
	public HttpRequestDecoderSpec initialBufferSize(int value) {
		return super.initialBufferSize(value);
	}

	@Override
	public HttpRequestDecoderSpec get() {
		return this;
	}

	/**
	 * Build a {@link Function} that applies the http request decoder configuration to a
	 * {@link TcpServer} by enriching its attributes.
	 */
	Function<TcpServer, TcpServer> build() {
		HttpRequestDecoderSpec decoder = new HttpRequestDecoderSpec();
		decoder.initialBufferSize = initialBufferSize;
		decoder.maxChunkSize = maxChunkSize;
		decoder.maxHeaderSize = maxHeaderSize;
		decoder.maxInitialLineLength = maxInitialLineLength;
		decoder.validateHeaders = validateHeaders;
		return tcp -> tcp.bootstrap(b -> HttpServerConfiguration.decoder(b, decoder));
	}

}