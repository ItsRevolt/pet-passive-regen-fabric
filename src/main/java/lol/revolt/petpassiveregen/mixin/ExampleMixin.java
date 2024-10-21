package lol.revolt.petpassiveregen.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.AxolotlEntity;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.entity.passive.OcelotEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.UUID;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import lol.revolt.petpassiveregen.test;
import net.minecraft.util.math.random.Random;

@Mixin(AnimalEntity.class)
public class ExampleMixin {
	@Inject(at = @At("TAIL"), method = "tickMovement")

	private void injectHealing(CallbackInfo info) {
		AnimalEntity entity = (AnimalEntity) (Object) this;
		if (entity.getHealth() >= entity.getMaxHealth())
			return;

		// Rideable tames automatically have regeneration
		// includes horses, donkeys, mules, skeleton horses and llamas
		// all these are based on AbstractHorseEntity
		if (entity instanceof AbstractHorseEntity)
			return;
		if (entity instanceof TameableEntity) {
			TameableEntity tameableEntity = (TameableEntity) (Object) entity;
			handleTameableEntity(tameableEntity);
		} else if (entity instanceof AxolotlEntity) {
			AxolotlEntity axolotlEntity = (AxolotlEntity) (Object) entity;
			handleAxolotlEntity(axolotlEntity);
		} else if (entity instanceof FoxEntity) {
			FoxEntity foxEntity = (FoxEntity) (Object) entity;
			handleFoxEntity(foxEntity);
		} else if (entity instanceof OcelotEntity) {
			OcelotEntity ocelotEntity = (OcelotEntity) (Object) entity;
			handleOcelotEntity(ocelotEntity);
		}
	}

	private void handleTameableEntity(TameableEntity self) {
		if (!self.getWorld().isClient && self.isAlive() && self.isTamed()) {
			attemptHeal(self);
		}
	}

	private void handleAxolotlEntity(AxolotlEntity self) {
		if (!self.getWorld().isClient && self.isAlive() && self.isFromBucket()) {
			attemptHeal(self);
		}
	}

	private void handleOcelotEntity(OcelotEntity self) {
		if (!self.getWorld().isClient && self.isAlive()) {
			boolean isTrusting = ((OcelotEntityAccessor) self).invokeIsTrusting();
            //boolean isTrusting = self.isTrusting();
            if (!isTrusting) return;
            attemptHeal(self);
        }
	}

	private void handleFoxEntity(FoxEntity self) {
		List<UUID> uuids = ((FoxEntityAccessor) self).invokeGetTrustedUuids();
		
		if (!self.getWorld().isClient && self.isAlive() && !uuids.isEmpty()) {
			attemptHeal(self);
		}
	}

	@Unique
	private void attemptHeal(AnimalEntity entity) {
		System.out.println("Attempting to heal: " + entity.getType());
    if (entity.getRandom().nextInt(900) == 0 && entity.deathTime == 0) {
        entity.heal(1.0F);
        System.out.println("Healed: " + entity.getType());
    }
	}
}