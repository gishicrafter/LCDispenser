package lcdispenser;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.liquids.ILiquidTank;
import net.minecraftforge.liquids.ITankContainer;
import net.minecraftforge.liquids.LiquidContainerRegistry;
import net.minecraftforge.liquids.LiquidStack;
import buildcraft.api.gates.ITriggerDirectional;
import buildcraft.api.gates.ITriggerParameter;
import buildcraft.api.gates.Trigger;

public class TriggerCanFillContainer extends Trigger implements ITriggerDirectional {

	public TriggerCanFillContainer(int id) {
		super(id);
	}

	@Override
	public String getTextureFile() {
		return CommonProxy.GATES_PNG;
	}

	@Override
	public int getIndexInTexture() {
		return 1;
	}

	@Override
	public boolean hasParameter() {
		return true;
	}

	@Override
	public String getDescription() {
		return "Has Enough Liquid For Container";
	}

	@Override
	public boolean isTriggerActive(ForgeDirection side, TileEntity tile, ITriggerParameter parameter)
	{
		if(tile instanceof ITankContainer)
		{
			ITankContainer tank = (ITankContainer)tile;
			ItemStack param = null;
			if(parameter != null) param = parameter.getItem();
			LiquidStack liquidToDrain;
			LiquidStack liquidDrained;
			if(LiquidContainerRegistry.isFilledContainer(param))
			{
				liquidToDrain = LiquidContainerRegistry.getLiquidForFilledItem(param);
				liquidDrained = tank.drain(side, liquidToDrain.amount, false);
				return liquidDrained != null && liquidDrained.isLiquidEqual(liquidToDrain) && liquidDrained.amount == liquidToDrain.amount;
			}
			else if(LiquidContainerRegistry.isEmptyContainer(param))
			{
				boolean result = false;
				for(ILiquidTank slot : tank.getTanks(side))
				{
					if(slot.getLiquid() != null)
					{
						LiquidStack liquidInTank = slot.getLiquid().copy();
						liquidInTank.amount = slot.getCapacity();
						ItemStack filled = LiquidContainerRegistry.fillLiquidContainer(liquidInTank, param);
						if(filled != null)
						{
							liquidToDrain = LiquidContainerRegistry.getLiquidForFilledItem(filled);
							liquidDrained = tank.drain(side, liquidToDrain.amount, false);
							result |= liquidDrained != null && liquidDrained.isLiquidEqual(liquidToDrain) && liquidDrained.amount == liquidToDrain.amount;
						}
					}
				}
				return result;
			}
			else
			{
				liquidDrained = tank.drain(side, LiquidContainerRegistry.BUCKET_VOLUME, false);
				return liquidDrained != null && liquidDrained.amount == LiquidContainerRegistry.BUCKET_VOLUME;
			}
		}
		return false;
	}

}
