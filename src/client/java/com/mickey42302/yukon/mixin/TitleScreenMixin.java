package com.mickey42302.yukon.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.SharedConstants;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.network.chat.Component;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {

	@Unique
	private static final boolean MC_DEBUG_TEST_WORLD = Boolean.getBoolean("MC_DEBUG_TEST_WORLD")
			|| Boolean.parseBoolean(System.getenv("MC_DEBUG_TEST_WORLD"));

	protected TitleScreenMixin(Component title) {
		super(title);
	}

	@Inject(
			method = "createNormalMenuOptions",
			at = @At(
					value = "FIELD",
					target = "Lnet/minecraft/SharedConstants;IS_RUNNING_IN_IDE:Z",
					ordinal = 0,
					opcode = Opcodes.GETSTATIC)
	)
	private void injectTestWorldButton(int topPos, int spacing, CallbackInfoReturnable<Integer> cir, @Local(name = "singleplayerButton") Button singleplayerButton) {
		if (MC_DEBUG_TEST_WORLD && SharedConstants.DEBUG_ENABLED && !net.minecraft.SharedConstants.IS_RUNNING_IN_IDE) {
			this.addRenderableWidget(
					Button.builder(
									Component.literal("TW"),
                                    _ -> CreateWorldScreen.testWorld(this.minecraft, () -> {})
							)
							.bounds(singleplayerButton.getX() + singleplayerButton.getWidth() + 2, topPos, 20, 20)
							.build()
			);
		}
	}
}