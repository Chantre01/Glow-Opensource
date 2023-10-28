package cn.utils.drawHelpers.shaderHelper.shaders;

import cn.utils.drawHelpers.shaderHelper.FramebufferShader;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL20;

public class FlowShader extends FramebufferShader {
     public static final FlowShader INSTANCE = new FlowShader();
     public float time;

     public FlowShader() {
          super("space.frag");
     }

     public void setupUniforms() {
          this.setupUniform("resolution");
          this.setupUniform("time");
     }

     public void updateUniforms() {
          GL20.glUniform2f(this.getUniform("resolution"), (float)(new ScaledResolution(mc)).getScaledWidth(), (float)(new ScaledResolution(mc)).getScaledHeight());
          GL20.glUniform1f(this.getUniform("time"), 1.0F);
     }
}
