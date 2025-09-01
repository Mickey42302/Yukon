package com.mickey42302.yukon.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin({Keyboard.class})
public abstract class KeyboardMixin {

    @Shadow public abstract void setClipboard(String clipboard);

    @Shadow protected abstract void debugLog(Text text);

    @WrapOperation(method = {"onKey"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/client/Keyboard;processF3(I)Z")})

    private boolean yukon$f3Key(Keyboard keyboard, int key, Operation<Boolean> original) {
        if (key == 301) {
            assert MinecraftClient.getInstance().player != null;
            this.debugLog(Text.translatable("yukon.debug.help.message"));
            MinecraftClient.getInstance().player.sendMessage(Text.translatable("yukon.debug.cycle_renderdistance.help"), false);
            MinecraftClient.getInstance().player.sendMessage(Text.translatable("yukon.debug.uuid.help"), false);
            MinecraftClient.getInstance().player.sendMessage(Text.translatable("yukon.debug.help.help"), false);

        }
        if (key == 89) {
            MinecraftClient client = MinecraftClient.getInstance();
            assert client.player != null;
            String uuid = client.player.getUuidAsString();
            setClipboard(uuid);
            assert MinecraftClient.getInstance().player != null;
            MinecraftClient.getInstance().player.sendMessage(Text.translatable("yukon.debug.uuid.copied"), false);
        }
        if (key == 82) {
            MinecraftClient client = MinecraftClient.getInstance();
            Integer currentViewDistance = client.options.getViewDistance().getValue();
            if (Screen.hasShiftDown()) {
                assert MinecraftClient.getInstance().player != null;
                if (currentViewDistance <= 31) {
                    client.options.getViewDistance().setValue(currentViewDistance - 1);
                } else {
                    client.options.getViewDistance().setValue(2);
                }
                this.debugLog(Text.translatable("yukon.debug.cycle_renderdistance.message", currentViewDistance.toString()));
            }
            else {
                assert MinecraftClient.getInstance().player != null;
                if (currentViewDistance <= 31) {
                    client.options.getViewDistance().setValue(currentViewDistance + 1);
                } else {
                    client.options.getViewDistance().setValue(2);
                }
                this.debugLog(Text.translatable("yukon.debug.cycle_renderdistance.message", currentViewDistance.toString()));
            }
        }
        return (processDebugKeys(key) || original.call(keyboard, key));
    }

    @Shadow
    protected abstract boolean processDebugKeys(int paramInt);
}
