package io.github.phantamanta44.wtflux.renderer;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface IIconRegister
{
    IIcon registerIcon(String p_94245_1_);
}