package com.github.hansi132.discordfab.discordbot.mixin;

import com.github.hansi132.discordfab.DiscordFab;
import net.minecraft.entity.damage.DamageTracker;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {
    @Redirect(method = "onDeath", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/damage/DamageTracker;getDeathMessage()Lnet/minecraft/text/Text;"))
    private Text inject$OnDeath(DamageTracker tracker) {
        final Text text = tracker.getDeathMessage();
        DiscordFab.getInstance().getChatSynchronizer().broadcast(text.getString());
        return text;
    }
}
