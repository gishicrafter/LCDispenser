package lcdispenser;

import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.liquids.ITankContainer;
import net.minecraftforge.liquids.LiquidContainerData;
import net.minecraftforge.liquids.LiquidContainerRegistry;
import net.minecraftforge.liquids.LiquidStack;

public class BehaviorContainerEmptyDispense extends BehaviorContainerDispense
{
	public BehaviorContainerEmptyDispense(IBehaviorDispenseItem chain)
	{
		super(chain);
	}
	
	@Override
	public ItemStack dispenseStack(IBlockSource blockSource, ItemStack itemstack)
	{
		EnumFacing facing = EnumFacing.func_82600_a(blockSource.func_82620_h());
		World world = blockSource.getWorld();
		int targetX = blockSource.getXInt() + facing.func_82601_c();
		int targetY = blockSource.getYInt();
		int targetZ = blockSource.getZInt() + facing.func_82599_e();
		
		TileEntity te = world.getBlockTileEntity(targetX, targetY, targetZ);
		if(te != null && te instanceof ITankContainer){
			for(LiquidContainerData data : LiquidContainerRegistry.getRegisteredLiquidContainerData()){
				if(data.container.isItemEqual(itemstack)){
					int amount = data.stillLiquid.amount;
					LiquidStack liquid = ((ITankContainer)te).drain(ForgeDirection.getOrientation(facing.ordinal()).getOpposite(), amount, false);
					if(liquid != null && liquid.isLiquidEqual(data.stillLiquid) && liquid.amount == amount){
						((ITankContainer)te).drain(ForgeDirection.getOrientation(facing.ordinal()).getOpposite(), amount, true);
						itemstack.stackSize--;
						ItemStack result = data.filled.copy();
						result.stackSize = 1;
						return result;
					}
				}
			}
		}
		System.out.println("Not matched");
		
		return null;
	}
}
