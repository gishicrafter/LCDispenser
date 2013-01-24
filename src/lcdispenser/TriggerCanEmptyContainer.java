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

public class TriggerCanEmptyContainer extends Trigger implements ITriggerDirectional
{

	public TriggerCanEmptyContainer(int id)
	{
		super(id);
	}

	@Override
	public String getTextureFile()
	{
		return CommonProxy.GATES_PNG;
	}

	@Override
	public int getIndexInTexture()
	{
		return 0;
	}

	@Override
	public boolean hasParameter()
	{
		return true;
	}

	@Override
	public String getDescription()
	{
		return "Has Enough Space For Liquid";
	}

	@Override
	public boolean isTriggerActive(ForgeDirection side, TileEntity tile, ITriggerParameter parameter)
	{
		if(tile instanceof ITankContainer)
		{
			ITankContainer tank = (ITankContainer)tile;
			ItemStack param = null;
			if(parameter != null) param = parameter.getItem();
			LiquidStack liquidToFill;
			int amountFilled;
			if(LiquidContainerRegistry.isFilledContainer(param))
			{
				liquidToFill = LiquidContainerRegistry.getLiquidForFilledItem(param);
				amountFilled = tank.fill(side, liquidToFill, false);
				return amountFilled == liquidToFill.amount;
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
							liquidToFill = LiquidContainerRegistry.getLiquidForFilledItem(filled);
							amountFilled = tank.fill(side, liquidToFill, false);
							result |= amountFilled == liquidToFill.amount;
						}
					}
					else
					{
						result |= slot.getCapacity() >= LiquidContainerRegistry.BUCKET_VOLUME;
					}
				}
				return result;
			}
			else
			{
				boolean result = false;
				for(ILiquidTank slot : tank.getTanks(side))
				{
					if(slot.getLiquid() != null)
					{
						result |= slot.getCapacity() - slot.getLiquid().amount >= LiquidContainerRegistry.BUCKET_VOLUME;
					}
					else
					{
						result |= slot.getCapacity() >= LiquidContainerRegistry.BUCKET_VOLUME;
					}
				}
				return result;
			}
		}
		return false;
	}

}
