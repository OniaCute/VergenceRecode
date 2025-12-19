package cc.vergence.utils.render;

import cc.vergence.injections.accessors.render.WorldRendererAccessor;
import cc.vergence.utils.interfaces.IMinecraft;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Render3DUtil implements IMinecraft {
    public static final Matrix4f lastProjMat = new Matrix4f();
    public static final Matrix4f lastModMat = new Matrix4f();
    public static final Matrix4f lastWorldSpaceMatrix = new Matrix4f();

    public static List<VertexCollection> QUADS = new ArrayList<>();
    public static List<VertexCollection> LINES = new ArrayList<>();
    public static List<VertexCollection> SHINE_QUADS = new ArrayList<>();
    public static List<VertexCollection> SHINE_LINES = new ArrayList<>();

    public static void enableRender(boolean depthTest) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        if (depthTest) {
            RenderSystem.disableDepthTest();
            RenderSystem.depthMask(false);
        }
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
    }

    public static void enableRender() {
        enableRender(false);
    }

    public static void disableRender(boolean depthTest) {
        RenderSystem.disableBlend();
        if (depthTest) {
            RenderSystem.enableDepthTest();
            RenderSystem.depthMask(true);
        }
    }

    public static void disableRender() {
        disableRender(false);
    }

    public static void init() {
        QUADS = new ArrayList<>();
        LINES = new ArrayList<>();
        SHINE_QUADS = new ArrayList<>();
        SHINE_LINES = new ArrayList<>();
    }

    public static boolean isFrustumVisible(Box box) {
        return ((WorldRendererAccessor) mc.worldRenderer).getFrustum().isVisible(box);
    }

    private static Vec3d cameraTransform(Vec3d vec3d) {
        Vec3d camera = mc.gameRenderer.getCamera().getPos();
        return new Vec3d(vec3d.x - camera.getX(), vec3d.y - camera.getY(), vec3d.z - camera.getZ());
    }

    private static Box cameraTransform(Box box) {
        Vec3d camera = mc.gameRenderer.getCamera().getPos();
        return new Box(box.minX - camera.getX(), box.minY - camera.getY(), box.minZ - camera.getZ(), box.maxX - camera.getX(), box.maxY - camera.getY(), box.maxZ - camera.getZ());
    }

    public static void addRenderLine(MatrixStack matrices, Vec3d from, Vec3d to, Color color) {
        Matrix4f matrix = matrices.peek().getPositionMatrix();
        from = cameraTransform(from);
        to = cameraTransform(to);
        LINES.add(new VertexCollection(new Vertex(matrix, (float) from.x, (float) from.y, (float) from.z, color), new Vertex(matrix, (float) to.x, (float) to.y, (float) to.z, color)));
    }

    public static void draw3DBox(MatrixStack matrixStack, Box box, Color color) {
        draw3DBox(matrixStack, box, color, true, color, true);
    }

    public static void draw3DBox(MatrixStack matrixStack, Box box, Color color, Color outlineColor) {
        draw3DBox(matrixStack, box, color, true, outlineColor, true);
    }

    public static void draw3DBox(MatrixStack matrixStack, Box box, Color color, boolean fill, Color outlineColor, boolean outline) {
        box = box.offset(mc.gameRenderer.getCamera().getPos().negate());
        enableRender();
        Matrix4f matrix = matrixStack.peek().getPositionMatrix();
        Tessellator tessellator = RenderSystem.renderThreadTesselator();
        BufferBuilder bufferBuilder = tessellator.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION);
        if (outline) {
            RenderSystem.setShaderColor(outlineColor.getRed() / 255f, outlineColor.getGreen() / 255f, outlineColor.getBlue() / 255f, outlineColor.getAlpha() / 255f);
            RenderSystem.setShader(ShaderProgramKeys.POSITION);
            tessellator.begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION);
            bufferBuilder.vertex(matrix, (float) box.minX, (float) box.minY, (float) box.minZ);
            bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.minY, (float) box.minZ);
            bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.minY, (float) box.minZ);
            bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.minY, (float) box.maxZ);
            bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.minY, (float) box.maxZ);
            bufferBuilder.vertex(matrix, (float) box.minX, (float) box.minY, (float) box.maxZ);
            bufferBuilder.vertex(matrix, (float) box.minX, (float) box.minY, (float) box.maxZ);
            bufferBuilder.vertex(matrix, (float) box.minX, (float) box.minY, (float) box.minZ);
            bufferBuilder.vertex(matrix, (float) box.minX, (float) box.minY, (float) box.minZ);
            bufferBuilder.vertex(matrix, (float) box.minX, (float) box.maxY, (float) box.minZ);
            bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.minY, (float) box.minZ);
            bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.maxY, (float) box.minZ);
            bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.minY, (float) box.maxZ);
            bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.maxY, (float) box.maxZ);
            bufferBuilder.vertex(matrix, (float) box.minX, (float) box.minY, (float) box.maxZ);
            bufferBuilder.vertex(matrix, (float) box.minX, (float) box.maxY, (float) box.maxZ);
            bufferBuilder.vertex(matrix, (float) box.minX, (float) box.maxY, (float) box.minZ);
            bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.maxY, (float) box.minZ);
            bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.maxY, (float) box.minZ);
            bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.maxY, (float) box.maxZ);
            bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.maxY, (float) box.maxZ);
            bufferBuilder.vertex(matrix, (float) box.minX, (float) box.maxY, (float) box.maxZ);
            bufferBuilder.vertex(matrix, (float) box.minX, (float) box.maxY, (float) box.maxZ);
            bufferBuilder.vertex(matrix, (float) box.minX, (float) box.maxY, (float) box.minZ);
            BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
        }
        disableRender();
        enableRender();
        matrix = matrixStack.peek().getPositionMatrix();
        tessellator = RenderSystem.renderThreadTesselator();
        bufferBuilder = tessellator.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION);
        if (fill) {
            RenderSystem.setShaderColor(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
            RenderSystem.setShader(ShaderProgramKeys.POSITION);
            tessellator.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION);
            bufferBuilder.vertex(matrix, (float) box.minX, (float) box.minY, (float) box.minZ);
            bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.minY, (float) box.minZ);
            bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.minY, (float) box.maxZ);
            bufferBuilder.vertex(matrix, (float) box.minX, (float) box.minY, (float) box.maxZ);
            bufferBuilder.vertex(matrix, (float) box.minX, (float) box.maxY, (float) box.minZ);
            bufferBuilder.vertex(matrix, (float) box.minX, (float) box.maxY, (float) box.maxZ);
            bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.maxY, (float) box.maxZ);
            bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.maxY, (float) box.minZ);
            bufferBuilder.vertex(matrix, (float) box.minX, (float) box.minY, (float) box.minZ);
            bufferBuilder.vertex(matrix, (float) box.minX, (float) box.maxY, (float) box.minZ);
            bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.maxY, (float) box.minZ);
            bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.minY, (float) box.minZ);
            bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.minY, (float) box.minZ);
            bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.maxY, (float) box.minZ);
            bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.maxY, (float) box.maxZ);
            bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.minY, (float) box.maxZ);
            bufferBuilder.vertex(matrix, (float) box.minX, (float) box.minY, (float) box.maxZ);
            bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.minY, (float) box.maxZ);
            bufferBuilder.vertex(matrix, (float) box.maxX, (float) box.maxY, (float) box.maxZ);
            bufferBuilder.vertex(matrix, (float) box.minX, (float) box.maxY, (float) box.maxZ);
            bufferBuilder.vertex(matrix, (float) box.minX, (float) box.minY, (float) box.minZ);
            bufferBuilder.vertex(matrix, (float) box.minX, (float) box.minY, (float) box.maxZ);
            bufferBuilder.vertex(matrix, (float) box.minX, (float) box.maxY, (float) box.maxZ);
            bufferBuilder.vertex(matrix, (float) box.minX, (float) box.maxY, (float) box.minZ);
            BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
        }
        disableRender();
    }

    public static void render(java.util.List<VertexCollection> quads, List<VertexCollection> debugLines, boolean shine) {
        enableRender();
        if (shine) {
            RenderSystem.blendFunc(770, 32772); // shine blend func
        } else {
            RenderSystem.blendFuncSeparate(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SrcFactor.ONE, GlStateManager.DstFactor.ZERO);
        }
        if (!quads.isEmpty()) {
            BufferBuilder buffer = RenderSystem.renderThreadTesselator().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
            for (VertexCollection collection : quads) {
                collection.vertex(buffer);
            }
            RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
            RenderSystem.disableCull();
            BufferRenderer.drawWithGlobalProgram(buffer.end());
            RenderSystem.enableCull();
        }
        if (!debugLines.isEmpty()) {
            BufferBuilder buffer = RenderSystem.renderThreadTesselator().begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
            for (VertexCollection collection : debugLines) {
                collection.vertex(buffer);
            }
            RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
            GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
            GL11.glEnable(GL11.GL_LINE_SMOOTH);
            BufferRenderer.drawWithGlobalProgram(buffer.end());
            GL11.glDisable(GL11.GL_LINE_SMOOTH);
        }
        disableRender();
    }

    public static Vec3d interpolatePos(float prevPosX, float prevPosY, float prevPosZ, float posX, float posY, float posZ) {
        double x = prevPosX + ((posX - prevPosX) * mc.getRenderTickCounter().getTickDelta(true)) - mc.getEntityRenderDispatcher().camera.getPos().getX();
        double y = prevPosY + ((posY - prevPosY) * mc.getRenderTickCounter().getTickDelta(true)) - mc.getEntityRenderDispatcher().camera.getPos().getY();
        double z = prevPosZ + ((posZ - prevPosZ) * mc.getRenderTickCounter().getTickDelta(true)) - mc.getEntityRenderDispatcher().camera.getPos().getZ();
        return new Vec3d(x, y, z);
    }

    public record VertexCollection(Vertex... vertices) {
        public void vertex(BufferBuilder buffer) {
            for (Vertex vertex : vertices) {
                buffer.vertex(vertex.matrix, vertex.x, vertex.y, vertex.z).color(vertex.color.getRed(), vertex.color.getGreen(), vertex.color.getBlue(), vertex.color.getAlpha());
            }
        }
    }
    public record Vertex(Matrix4f matrix, float x, float y, float z, Color color) {}
}
