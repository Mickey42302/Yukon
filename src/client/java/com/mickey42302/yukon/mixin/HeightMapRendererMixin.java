package com.mickey42302.yukon.mixin;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.debug.HeightMapRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.gizmos.GizmoStyle;
import net.minecraft.gizmos.Gizmos;
import net.minecraft.util.ARGB;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@Mixin(HeightMapRenderer.class)
public abstract class HeightMapRendererMixin {

	@Shadow
	private Vector3f getColor(Heightmap.Types type) {
		throw new AssertionError();
	}

	/**
	 * @author Mickey42302
	 * @reason Add frustum culling and direct heightmap lookups to improve debug rendering performance.
	 */
	@Overwrite
	public void emitGizmos(double camX, double camY, double camZ, net.minecraft.util.debug.DebugValueAccess debugValues, Frustum frustum, float partialTicks) {
		HeightMapRenderer self = (HeightMapRenderer) (Object) this;
		net.minecraft.client.Minecraft client = net.minecraft.client.Minecraft.getInstance();
		ClientLevel clientLevel = client.level;
		if (clientLevel == null) return;

		BlockPos playerPos = BlockPos.containing(camX, 0.0D, camZ);

		for (int chunkX = -2; chunkX <= 2; chunkX++) {
			for (int chunkZ = -2; chunkZ <= 2; chunkZ++) {
				BlockPos offsetPos = playerPos.offset(chunkX * 16, 0, chunkZ * 16);
				ChunkAccess chunk = clientLevel.getChunk(offsetPos);
				ChunkPos chunkPos = chunk.getPos();

				int minBlockX = chunkPos.getMinBlockX();
				int minBlockZ = chunkPos.getMinBlockZ();
				AABB chunkBounds = new AABB(minBlockX, clientLevel.getMinY(), minBlockZ, minBlockX + 16, clientLevel.getMaxY(), minBlockZ + 16);
				if (!frustum.isVisible(chunkBounds)) {
					continue;
				}

				for (Map.Entry<Heightmap.Types, Heightmap> heightmapEntry : chunk.getHeightmaps()) {
					Heightmap.Types type = heightmapEntry.getKey();
					Heightmap heightmap = heightmapEntry.getValue();
					Vector3f color = getColor(type);
					int colorArgb = ARGB.colorFromFloat(1.0F, color.x(), color.y(), color.z());
					GizmoStyle style = GizmoStyle.fill(colorArgb);
					float typeOffset = type.ordinal() * 0.09375F;

					for (int relativeX = 0; relativeX < 16; relativeX++) {
						for (int relativeZ = 0; relativeZ < 16; relativeZ++) {
							int xx = SectionPos.sectionToBlockCoord(chunkPos.x(), relativeX);
							int zz = SectionPos.sectionToBlockCoord(chunkPos.z(), relativeZ);

							int rawHeight = heightmap.getFirstAvailable(relativeX, relativeZ);
							float height = rawHeight + typeOffset;

							AABB box = new AABB(xx + 0.25F, height, zz + 0.25F, xx + 0.75F, height + 0.09375F, zz + 0.75F);

							if (frustum.isVisible(box)) {
								Gizmos.cuboid(box, style);
							}
						}
					}
				}
			}
		}
	}
}