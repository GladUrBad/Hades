package me.apex.hades.check.impl.movement.fly;

import me.apex.hades.check.Check;
import me.apex.hades.check.ClassInterface;
import me.apex.hades.check.Type;
import me.apex.hades.event.AnticheatEvent;
import me.apex.hades.event.impl.packetevents.FlyingPacketEvent;
import me.apex.hades.user.User;
import me.apex.hades.utils.time.TimeUtils;

public class FlyE extends Check implements ClassInterface {
    public FlyE(String checkName, String letter, Type type, boolean enabled) {
        super(checkName, letter, type, enabled);
    }

    @Override
    public void onHandle(User user, AnticheatEvent e) {
        if (e instanceof FlyingPacketEvent) {
            if (user != null) {
                if (TimeUtils.elapsed(user.lastVelocity) < 1000L || user.getBlockData().climbableTicks > 0 || user.getBlockData().liquidTicks > 0|| TimeUtils.elapsed(user.getLastFullTeleport()) < 1000L) {
                    return;
                }
                if (user.isClientGround() && user.isLastClientGround() && !user.isOnGround() && user.isLastOnGround()) {
                    if (++preVL > 4) {
                        flag(user, "Spoofing Ground");
                    }
                }else preVL -= Math.min(preVL, 0.5);
            }
        }
    }
}