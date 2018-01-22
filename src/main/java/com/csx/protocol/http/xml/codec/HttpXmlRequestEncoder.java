package com.csx.protocol.http.xml.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

import java.net.InetAddress;
import java.util.List;

import static io.netty.handler.codec.http.HttpHeaderNames.*;
import static io.netty.handler.codec.http.HttpHeaderValues.CLOSE;
import static io.netty.handler.codec.http.HttpHeaderValues.DEFLATE;
import static io.netty.handler.codec.http.HttpHeaderValues.GZIP;
/**
 * Created with IntelliJ IDEA.
 *
 * @Description: TODO
 * @Author: csx
 * @Date: 2018/01/22
 */
public class HttpXmlRequestEncoder extends
        AbstractHttpXmlEncoder<HttpXmlRequest> {

    @Override
    protected void encode(ChannelHandlerContext ctx, HttpXmlRequest msg,
                          List<Object> out) throws Exception {
        ByteBuf body = encode0(ctx, msg.getBody());
        FullHttpRequest request = msg.getRequest();
        if (request == null) {
            request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1,
                    HttpMethod.GET, "/do", body);
            HttpHeaders headers = request.headers();
            headers.set(HOST, InetAddress.getLocalHost()
                    .getHostAddress());
            headers.set(CONNECTION, CLOSE);
            headers.set(ACCEPT_ENCODING,
                    GZIP.toString() + ','
                            + DEFLATE.toString());
            headers.set(ACCEPT_CHARSET,
                    "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
            headers.set(ACCEPT_LANGUAGE, "zh");
            headers.set(USER_AGENT,
                    "Netty xml Http Client side");
            headers.set(ACCEPT,
                    "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        }
        HttpHeaderUtil.setContentLength(request,body.readableBytes());
//        HttpHeaders.setContentLength(request, body.readableBytes());
        out.add(request);
    }

}
