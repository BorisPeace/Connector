package org.eclipse.iofog.comsat.restapi;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.ssl.SslContext;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

/**
 * Created by Saeid on 6/26/2016.
 */
public class RestAPIChannelInitializer extends ChannelInitializer<SocketChannel> {
	private final EventExecutorGroup executor;
	private final SslContext sslCtx;

	public RestAPIChannelInitializer(SslContext sslCtx) {
		this.executor = new DefaultEventExecutorGroup(10);
		this.sslCtx = sslCtx;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline p = ch.pipeline();
        if (sslCtx != null)
        	p.addLast(sslCtx.newHandler(ch.alloc()));
        p.addLast(new HttpServerCodec());
		p.addLast(new HttpObjectAggregator(65535));
		p.addLast(new RestAPIChannelHandler(executor, sslCtx));
    }
}