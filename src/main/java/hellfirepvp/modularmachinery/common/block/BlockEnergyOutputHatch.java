/*******************************************************************************
 * HellFirePvP / Modular Machinery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/ModularMachinery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.modularmachinery.common.block;

import hellfirepvp.modularmachinery.common.CommonProxy;
import hellfirepvp.modularmachinery.common.block.prop.EnergyHatchSize;
import hellfirepvp.modularmachinery.common.tiles.TileEnergyOutputHatch;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

/**
 * This class is part of the Modular Machinery Mod
 * The complete source code for this mod can be found on github.
 * Class: BlockEnergyOutputHatch
 * Created by HellFirePvP
 * Date: 08.07.2017 / 10:52
 */
public class BlockEnergyOutputHatch extends BlockContainer implements BlockCustomName {

    private static final PropertyEnum<EnergyHatchSize> BUS_TYPE = PropertyEnum.create("size", EnergyHatchSize.class);

    public BlockEnergyOutputHatch() {
        super(Material.IRON);
        setCreativeTab(CommonProxy.creativeTabModularMachinery);
    }

    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
        for (EnergyHatchSize size : EnergyHatchSize.values()) {
            items.add(new ItemStack(this, 1, size.ordinal()));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        EnergyHatchSize size = EnergyHatchSize.values()[MathHelper.clamp(stack.getMetadata(), 0, EnergyHatchSize.values().length - 1)];
        tooltip.add(TextFormatting.GRAY + "Stores " + size.maxEnergy + " RF/FE");
        tooltip.add(TextFormatting.GRAY + "Transfers " + size.transferLimit + " RF/FE per tick");
        if(Loader.isModLoaded("ic2")) {
            tooltip.add("");
            tooltip.add(TextFormatting.GRAY + "Output Voltage: " + TextFormatting.BLUE + size.getEnergyDescriptor());
            tooltip.add(TextFormatting.GRAY + "Output Transfer: " + TextFormatting.BLUE + size.getEnergyTransmission() + " EU/t");
        }
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(BUS_TYPE, EnergyHatchSize.values()[meta]);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(BUS_TYPE).ordinal();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, BUS_TYPE);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEnergyOutputHatch(state.getValue(BUS_TYPE));
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return null;
    }

    @Override
    public String getIdentifierForMeta(int meta) {
        return getStateFromMeta(meta).getValue(BUS_TYPE).getName();
    }

}