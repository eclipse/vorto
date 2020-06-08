/**
 * Copyright (c) 2020 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.vorto.repository.utils;

import org.eclipse.vorto.repository.core.events.AppEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;

import java.util.Collection;

public class MockAppEventPublisher implements ApplicationEventPublisher {

    private Collection<ApplicationListener<AppEvent>> listeners;

    public MockAppEventPublisher(Collection<ApplicationListener<AppEvent>> listeners) {
        this.listeners = listeners;
    }

    @Override
    public void publishEvent(ApplicationEvent event) {
        if (event instanceof AppEvent) {
            AppEvent appEvent = (AppEvent) event;
            for(ApplicationListener<AppEvent> listener : listeners) {
                listener.onApplicationEvent(appEvent);
            }
        }
    }

    @Override
    public void publishEvent(Object event) {
        // implement when need arises
    }
}
