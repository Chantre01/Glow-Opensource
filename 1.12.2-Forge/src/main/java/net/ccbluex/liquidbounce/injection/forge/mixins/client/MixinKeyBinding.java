package net.ccbluex.liquidbounce.injection.forge.mixins.client;


import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.modules.movement.GuiMove;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.settings.IKeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(KeyBinding.class)
public abstract class MixinKeyBinding {

    @Shadow
    public boolean pressed;
    
    @Overwrite
    public boolean isKeyDown() {
        GuiMove GuiMove = (GuiMove) LiquidBounce.moduleManager.get(GuiMove.class);
        boolean InvMove = GuiMove.getState() ? this.pressed : this.pressed && this.getKeyConflictContext().isActive() && this.getKeyModifier().isActive(this.getKeyConflictContext());
        return InvMove;
    }

    @Shadow
    public abstract IKeyConflictContext getKeyConflictContext();

    @Shadow
    public abstract KeyModifier getKeyModifier();

}
