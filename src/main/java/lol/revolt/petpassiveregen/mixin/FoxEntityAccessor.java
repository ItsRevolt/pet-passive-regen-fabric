package lol.revolt.petpassiveregen.mixin;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.passive.FoxEntity;

import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(FoxEntity.class)
public interface FoxEntityAccessor {
	@Invoker("getTrustedUuids")
	public List<UUID> invokeGetTrustedUuids();
}