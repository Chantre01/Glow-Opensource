package cn.utils.drawHelpers.shaderHelper;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import net.ccbluex.liquidbounce.utils.ClientUtils;
import org.apache.commons.io.IOUtils;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public abstract class Shader {
     private int program;
     private Map<String, Integer> uniformsMap;

     public Shader(String fragmentShader) {
          int vertexShaderID;
          int fragmentShaderID;
          try {
               InputStream vertexStream = this.getClass().getResourceAsStream("/assets/minecraft/liquidbounce/drawhelp/shaders/vertex.vert");
               vertexShaderID = this.createShader(IOUtils.toString(vertexStream), 35633);
               IOUtils.closeQuietly(vertexStream);
               InputStream fragmentStream = this.getClass().getResourceAsStream("/assets/minecraft/liquidbounce/drawhelp/shaders/fragment/" + fragmentShader);
               fragmentShaderID = this.createShader(IOUtils.toString(fragmentStream), 35632);
               IOUtils.closeQuietly(fragmentStream);
          } catch (Exception var6) {
               return;
          }

          if(vertexShaderID == 0 || fragmentShaderID == 0)
               return;

          program = ARBShaderObjects.glCreateProgramObjectARB();

          if(program == 0)
               return;

          ARBShaderObjects.glAttachObjectARB(program, vertexShaderID);
          ARBShaderObjects.glAttachObjectARB(program, fragmentShaderID);

          ARBShaderObjects.glLinkProgramARB(program);
          ARBShaderObjects.glValidateProgramARB(program);

          ClientUtils.getLogger().info("[Shader] Successfully loaded: " + fragmentShader);

     }
     public void startShader() {
          GL11.glPushMatrix();
          GL20.glUseProgram(program);

          if(uniformsMap == null) {
               uniformsMap = new HashMap<>();
               setupUniforms();
          }

          updateUniforms();
     }

     public void stopShader() {
          GL20.glUseProgram(0);
          GL11.glPopMatrix();
     }

     public abstract void setupUniforms();

     public abstract void updateUniforms();


     private int createShader(String shaderSource, int shaderType) {
          int shader = 0;

          try {
               shader = ARBShaderObjects.glCreateShaderObjectARB(shaderType);

               if(shader == 0)
                    return 0;

               ARBShaderObjects.glShaderSourceARB(shader, shaderSource);
               ARBShaderObjects.glCompileShaderARB(shader);

               if(ARBShaderObjects.glGetObjectParameteriARB(shader, ARBShaderObjects.GL_OBJECT_COMPILE_STATUS_ARB) == GL11.GL_FALSE)
                    throw new RuntimeException("Error creating shader: " + getLogInfo(shader));

               return shader;
          }catch(final Exception e) {
               ARBShaderObjects.glDeleteObjectARB(shader);
               throw e;

          }
     }

     private String getLogInfo(int i) {
          return ARBShaderObjects.glGetInfoLogARB(i, ARBShaderObjects.glGetObjectParameteriARB(i, ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB));
     }
     public void setUniform(final String uniformName, final int location) {
          uniformsMap.put(uniformName, location);
     }

     public void setupUniform(final String uniformName) {
          setUniform(uniformName, GL20.glGetUniformLocation(program, uniformName));
     }

     public int getUniform(final String uniformName) {
          return uniformsMap.get(uniformName);
     }

     public int getProgramId() {
          return program;
     }
}
