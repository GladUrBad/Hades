package me.apex.hades.check.impl.combat.aura;

import me.apex.hades.check.api.Check;
import me.apex.hades.check.api.CheckInfo;
import me.apex.hades.objects.User;
import me.apex.hades.utils.MathUtils;
import me.purplex.packetevents.enums.EntityUseAction;
import me.purplex.packetevents.event.impl.PacketReceiveEvent;
import me.purplex.packetevents.packet.Packet;
import me.purplex.packetevents.packetwrappers.in.use_entity.impl.WrappedPacketInUseEntity;

import java.util.Deque;
import java.util.LinkedList;

@CheckInfo(name = "Aura", type = "G")
public class AuraG extends Check {

    private Deque<Long> diffs = new LinkedList();

    @Override
    public void onPacket(PacketReceiveEvent e, User user) {
        if (e.getPacketName().equalsIgnoreCase(Packet.Client.USE_ENTITY)) {
        	WrappedPacketInUseEntity packet = new WrappedPacketInUseEntity(e.getPacket());
            if (packet.action == EntityUseAction.ATTACK) {
                user.setLastHit(e.getTimestamp());
                diffs.add((long) user.getDeltaYaw());
                if (diffs.size() == 10) {
                    double deviation = MathUtils.getStandardDeviation(diffs.stream().mapToLong(l -> l).toArray());

                    if (deviation > 100)
                        flag(user, "deviation = " + deviation);

                    diffs.clear();
                }
            }
        }
    }

}