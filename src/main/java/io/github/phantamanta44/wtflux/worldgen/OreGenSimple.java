package io.github.phantamanta44.wtflux.worldgen;

import io.github.phantamanta44.wtflux.util.BlockWithMeta;

import java.util.Random;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

public class OreGenSimple implements IWorldGenerator {

    private final BlockWithMeta genBlock, replBlock;
    private final int dim;
    private final int minYLevel, maxYLevel;
    private final int iterations, veinSize;

    public OreGenSimple(BlockWithMeta blockToGen, int dimensionToGenerate, BlockWithMeta blockToReplace, int minY, int maxY, int perChunk, int veinDensity) {
        genBlock = blockToGen;
        dim = dimensionToGenerate;
        replBlock = blockToReplace;
        minYLevel = minY;
        maxYLevel = maxY;
        iterations = perChunk;
        veinSize = veinDensity;
    }

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        if (world.getWorldInfo().getWorldName() == "Overworld") {
            for(int i = 0; i < iterations; i++) {
                int x = chunkX * 16 + random.nextInt(16);
                int y = random.nextInt(maxYLevel - minYLevel) + minYLevel;
                int z = chunkZ * 16 + random.nextInt(16);
                BlockPos blockPos = new BlockPos(x, y, z);
                for (int j = 0; j < veinSize; j++) {
                    //if (world.getBlockState(blockPos).equals(replBlock.getBlock()) && world.getBlockState(blockPos) == replBlock.getMeta())
                        //world.setBlockState(blockPos, genBlock.getBlock(), genBlock.getMeta(), 0);
                    int dir = random.nextInt(3);
                    if (dir == 0)
                        x += random.nextBoolean() ? 1 : -1;
                    else if (dir == 1)
                        y += random.nextBoolean() ? 1 : -1;
                    else
                        z += random.nextBoolean() ? 1 : -1;
                }
            }
        }
    }
}