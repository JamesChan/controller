/*
 * Copyright (c) 2014 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.controller.sal.restconf.broker.client;

import org.opendaylight.controller.sal.binding.api.BindingAwareBroker.ConsumerContext;

public interface SalRemoteClient extends AutoCloseable {

    ConsumerContext registerConsumer();

}
