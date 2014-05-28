/*
* Copyright (c) 2014 Brocade Communications Systems, Inc. and others.  All rights reserved.
*
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License v1.0 which accompanies this distribution,
* and is available at http://www.eclipse.org/legal/epl-v10.html
*/
package org.opendaylight.controller.sal.restconf.rpc.impl;

import org.opendaylight.controller.sal.core.api.mount.MountInstance;
import org.opendaylight.controller.sal.restconf.impl.RestconfDocumentedException;
import org.opendaylight.yangtools.yang.common.RpcResult;
import org.opendaylight.yangtools.yang.data.api.CompositeNode;
import org.opendaylight.yangtools.yang.model.api.RpcDefinition;

import com.google.common.base.Preconditions;

/**
 * Provides an implementation which invokes rpc methods via a mounted yang data model.
 * @author Devin Avery
 *
 */
public class MountPointRpcExecutor extends AbstractRpcExecutor {
    private final MountInstance mountPoint;

    public MountPointRpcExecutor(RpcDefinition rpcDef, MountInstance mountPoint) {
        super( rpcDef );
        this.mountPoint = mountPoint;
        Preconditions.checkNotNull( mountPoint, "MountInstance can not be null." );
    }

    @Override
    public RpcResult<CompositeNode> invokeRpc( CompositeNode rpcRequest )
                                                   throws RestconfDocumentedException {
        return getRpcResult( mountPoint.rpc( getRpcDefinition().getQName(), rpcRequest ) );
    }
}