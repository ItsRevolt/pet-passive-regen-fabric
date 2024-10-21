package lol.revolt.petpassiveregen.mixin;

import java.util.List;
import java.util.UUID;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import net.minecraft.entity.passive.FoxEntity;

@Mixin(FoxEntity.class)
public interface FoxEntityAccessor {
	@Invoker("getTrustedUuids")
	public List<UUID> invokeGetTrustedUuids();
}