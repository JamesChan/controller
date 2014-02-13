/*
 * Copyright IBM Corporation, 2013.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.controller.md.statistics.manager;

import org.opendaylight.controller.sal.binding.api.data.DataModificationTransaction;
import org.opendaylight.controller.sal.binding.api.data.DataProviderService;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.inventory.rev130819.FlowCapableNode;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.inventory.rev130819.meters.Meter;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.inventory.rev130819.meters.MeterBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.inventory.rev130819.meters.MeterKey;
import org.opendaylight.yang.gen.v1.urn.opendaylight.inventory.rev130819.nodes.Node;
import org.opendaylight.yang.gen.v1.urn.opendaylight.meter.statistics.rev131111.NodeMeterStatistics;
import org.opendaylight.yang.gen.v1.urn.opendaylight.meter.statistics.rev131111.NodeMeterStatisticsBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.meter.statistics.rev131111.nodes.node.meter.MeterStatisticsBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.meter.types.rev130918.meter.statistics.reply.MeterStats;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;

final class MeterStatsTracker extends AbstractStatsTracker<MeterStats, MeterStats> {

    MeterStatsTracker(InstanceIdentifier<Node> nodeIdentifier, DataProviderService dps, long lifetimeNanos) {
        super(nodeIdentifier, dps, lifetimeNanos);
    }

    @Override
    protected void cleanupSingleStat(DataModificationTransaction trans, MeterStats item) {
        InstanceIdentifier<NodeMeterStatistics> meterRef = getNodeIdentifierBuilder()
                            .augmentation(FlowCapableNode.class)
                            .child(Meter.class,new MeterKey(item.getMeterId()))
                            .augmentation(NodeMeterStatistics.class).build();
        trans.removeOperationalData(meterRef);
    }

    @Override
    protected MeterStats updateSingleStat(DataModificationTransaction trans, MeterStats item) {
        MeterBuilder meterBuilder = new MeterBuilder();
        MeterKey meterKey = new MeterKey(item.getMeterId());
        meterBuilder.setKey(meterKey);

        InstanceIdentifier<Meter> meterRef = getNodeIdentifierBuilder()
                .augmentation(FlowCapableNode.class).child(Meter.class,meterKey).build();

        NodeMeterStatisticsBuilder meterStatsBuilder= new NodeMeterStatisticsBuilder();
        meterStatsBuilder.setMeterStatistics(new MeterStatisticsBuilder(item).build());

        //Update augmented data
        meterBuilder.addAugmentation(NodeMeterStatistics.class, meterStatsBuilder.build());
        trans.putOperationalData(meterRef, meterBuilder.build());
        return item;
    }
}