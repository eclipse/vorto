/**
 * Copyright (c) 2015-2016 Bosch Software Innovations GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Distribution License v1.0 which accompany this distribution.
 *
 * The Eclipse Public License is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * The Eclipse Distribution License is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * Contributors:
 * Bosch Software Innovations GmbH - Please refer to git log
 */
package org.eclipse.vorto.codegen.kura.templates

import org.eclipse.vorto.codegen.api.IFileTemplate
import org.eclipse.vorto.core.api.model.informationmodel.InformationModel
import org.eclipse.vorto.codegen.api.InvocationContext

class ManifestTemplate implements IFileTemplate<InformationModel> {
	
	override getFileName(InformationModel context) {
		'''MANIFEST.MF'''
	}
	
	override getPath(InformationModel context) {
		'''«Utils.basePath»/META-INF'''
	}
	
	override getContent(InformationModel element, InvocationContext context) {
'''
Manifest-Version: 1.0
Bundle-ManifestVersion: 2
Bundle-Name: «element.name»
Bundle-SymbolicName: «Utils.javaPackage»
Bundle-Version: 1.0.0.qualifier
Bundle-Vendor: Eclipse Vorto
Bundle-RequiredExecutionEnvironment: JavaSE-1.8
Service-Component: OSGI-INF/*.xml
Bundle-ActivationPolicy: lazy
«IF context.configurationProperties.getOrDefault("boschcloud","false").equalsIgnoreCase("true")»
Bundle-ClassPath: secret/, .,
 lib/async-http-client-2.0.0.jar,
 lib/cr-integration-api-3.3.0.jar,
 lib/cr-integration-client-2.4.1.jar,
 lib/cr-integration-client-osgi-2.4.1.jar,
 lib/cr-json-1.6.0.jar,
 lib/cr-model-3.3.0.jar,
 lib/javassist-3.20.0-GA.jar,
 lib/netty-buffer-4.0.36.Final.jar,
 lib/netty-codec-4.0.36.Final.jar,
 lib/netty-codec-dns-2.0.0.jar,
 lib/netty-codec-http-4.0.36.Final.jar,
 lib/netty-common-4.0.36.Final.jar,
 lib/netty-handler-4.0.36.Final.jar,
 lib/netty-reactive-streams-1.0.4.jar,
 lib/netty-resolver-2.0.0.jar,
 lib/netty-resolver-dns-2.0.0.jar,
 lib/netty-transport-4.0.36.Final.jar,
 lib/reactive-streams-1.0.0.jar,
 lib/reactor-bus-2.0.7.RELEASE.jar,
 lib/reactor-core-2.0.7.RELEASE.jar,
 lib/stomp-client-2.4.1.jar,
 lib/stomp-common-2.4.1.jar,
 lib/things-model-2.4.1.jar
«ENDIF»
Export-Package: 
 «Utils.javaPackage»,
«IF context.configurationProperties.getOrDefault("boschcloud","false").equalsIgnoreCase("true")» «Utils.javaPackage».cloud,
 «Utils.javaPackage».cloud.bosch,
 com.bosch.cr.integration,
 com.bosch.cr.integration.client,
 com.bosch.cr.integration.client.configuration,
 com.bosch.cr.integration.client.internal,
 com.bosch.cr.integration.client.messages,
 com.bosch.cr.integration.client.messages.internal,
 com.bosch.cr.integration.client.response,
 com.bosch.cr.integration.client.stomp,
 com.bosch.cr.integration.client.things,
 com.bosch.cr.integration.exceptions,
 com.bosch.cr.integration.messages,
 com.bosch.cr.integration.registration,
 com.bosch.cr.integration.things,
 com.bosch.cr.json,
 com.bosch.cr.model.acl,
 com.bosch.cr.model.acl.exceptions,
 com.bosch.cr.model.attributes,
 com.bosch.cr.model.authorization,
 com.bosch.cr.model.common,
 com.bosch.cr.model.exceptions,
 com.bosch.cr.model.json,
 com.bosch.cr.model.messages,
 com.bosch.cr.model.messages.exceptions,
 com.bosch.cr.model.relations,
 com.bosch.cr.model.relations.exceptions,
 com.bosch.cr.model.search,
 com.bosch.cr.model.things,
 com.bosch.cr.model.things.exceptions,
 com.bosch.cr.stomp.client.api,
 com.bosch.cr.stomp.client.exception,
 com.bosch.cr.stomp.client.impl,
 com.bosch.cr.stomp.common,
 com.bosch.cr.stomp.common.encoding,
 com.bosch.cr.things.model,
 com.bosch.cr.things.model.commands,
 com.bosch.cr.things.model.commands.modify,
 com.bosch.cr.things.model.commands.statistics,
 com.bosch.cr.things.model.commands.sudo,
 com.bosch.cr.things.model.commands.view,
 com.bosch.cr.things.model.events,
 com.bosch.cr.things.model.exceptions,
 com.typesafe.netty,
 io.netty.bootstrap,
 io.netty.buffer,
 io.netty.channel,
 io.netty.channel.embedded,
 io.netty.channel.group,
 io.netty.channel.local,
 io.netty.channel.nio,
 io.netty.channel.oio,
 io.netty.channel.pool,
 io.netty.channel.socket,
 io.netty.channel.socket.nio,
 io.netty.channel.socket.oio,
 io.netty.handler.codec,
 io.netty.handler.codec.base64,
 io.netty.handler.codec.bytes,
 io.netty.handler.codec.compression,
 io.netty.handler.codec.dns,
 io.netty.handler.codec.http,
 io.netty.handler.codec.http.cookie,
 io.netty.handler.codec.http.cors,
 io.netty.handler.codec.http.multipart,
 io.netty.handler.codec.http.websocketx,
 io.netty.handler.codec.marshalling,
 io.netty.handler.codec.protobuf,
 io.netty.handler.codec.rtsp,
 io.netty.handler.codec.serialization,
 io.netty.handler.codec.spdy,
 io.netty.handler.codec.string,
 io.netty.handler.ipfilter,
 io.netty.handler.logging,
 io.netty.handler.ssl,
 io.netty.handler.ssl.util,
 io.netty.handler.stream,
 io.netty.handler.timeout,
 io.netty.handler.traffic,
 io.netty.resolver,
 io.netty.resolver.dns,
 io.netty.util,
 io.netty.util.collection,
 io.netty.util.concurrent,
 io.netty.util.internal,
 io.netty.util.internal.chmv8,
 io.netty.util.internal.logging,
 javassist,
 javassist.bytecode,
 javassist.bytecode.analysis,
 javassist.bytecode.annotation,
 javassist.bytecode.stackmap,
 javassist.compiler,
 javassist.compiler.ast,
 javassist.convert,
 javassist.expr,
 javassist.runtime,
 javassist.scopedpool,
 javassist.tools,
 javassist.tools.reflect,
 javassist.tools.rmi,
 javassist.tools.web,
 javassist.util,
 javassist.util.proxy,
 org.asynchttpclient,
 org.asynchttpclient.channel,
 org.asynchttpclient.config,
 org.asynchttpclient.cookie,
 org.asynchttpclient.exception,
 org.asynchttpclient.filter,
 org.asynchttpclient.future,
 org.asynchttpclient.handler,
 org.asynchttpclient.handler.resumable,
 org.asynchttpclient.netty,
 org.asynchttpclient.netty.channel,
 org.asynchttpclient.netty.future,
 org.asynchttpclient.netty.handler,
 org.asynchttpclient.netty.handler.intercept,
 org.asynchttpclient.netty.request,
 org.asynchttpclient.netty.request.body,
 org.asynchttpclient.netty.ssl,
 org.asynchttpclient.netty.timeout,
 org.asynchttpclient.netty.ws,
 org.asynchttpclient.ntlm,
 org.asynchttpclient.oauth,
 org.asynchttpclient.proxy,
 org.asynchttpclient.request.body,
 org.asynchttpclient.request.body.generator,
 org.asynchttpclient.request.body.multipart,
 org.asynchttpclient.request.body.multipart.part,
 org.asynchttpclient.resolver,
 org.asynchttpclient.spnego,
 org.asynchttpclient.uri,
 org.asynchttpclient.util,
 org.asynchttpclient.webdav,
 org.asynchttpclient.ws,
 org.reactivestreams,
 reactor,
 reactor.bus,
 reactor.bus.filter,
 reactor.bus.publisher,
 reactor.bus.registry,
 reactor.bus.routing,
 reactor.bus.selector,
 reactor.bus.spec,
 reactor.bus.support,
 reactor.bus.timer,
 reactor.core,
 reactor.core.config,
 reactor.core.dispatch,
 reactor.core.dispatch.wait,
 reactor.core.internal,
 reactor.core.processor,
 reactor.core.processor.util,
 reactor.core.queue,
 reactor.core.queue.internal,
 reactor.core.reactivestreams,
 reactor.core.support,
 reactor.fn,
 reactor.fn.support,
 reactor.fn.timer,
 reactor.fn.tuple,
 reactor.io,
 reactor.io.buffer,
 reactor.io.codec,
 reactor.io.codec.compress,
 reactor.io.codec.json,
 reactor.io.codec.kryo,
 reactor.io.codec.protobuf,
 reactor.io.queue,
 reactor.io.queue.spec,
 reactor.jarjar.com.lmax.disruptor,
 reactor.jarjar.com.lmax.disruptor.collections,
 reactor.jarjar.com.lmax.disruptor.dsl,
 reactor.jarjar.com.lmax.disruptor.util,
 reactor.jarjar.jsr166e,
 reactor.jarjar.jsr166e.extra
«ELSE» «Utils.javaPackage».cloud
«ENDIF»
Import-Package: org.slf4j;version="1.7.21",
 org.eclipse.kura;version="1.3.0",
 org.eclipse.kura.configuration;version="1.1.2",
«IF context.configurationProperties.getOrDefault("bluetooth","false").equalsIgnoreCase("true")» org.eclipse.kura.bluetooth;version="1.4.0",
 org.eclipse.kura.bluetooth.listener;version="1.0.1",
«ENDIF»
«IF context.configurationProperties.getOrDefault("boschcloud","false").equalsIgnoreCase("true")» org.osgi.service.component,
 com.eclipsesource.json;version="0.9.4",
 javax.net.ssl,
 javax.security.cert
«ELSE» org.osgi.service.component,
 org.eclipse.kura.cloud;version="1.1.0",
 org.eclipse.kura.message;version="1.1.1"
«ENDIF»
'''
	}	
}