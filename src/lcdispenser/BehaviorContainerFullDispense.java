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

public class BehaviorContainerFullDispense extends BehaviorContainerDispense
{
	public BehaviorContainerFullDispense(IBehaviorDispenseItem chain)
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
				if(data.filled.isItemEqual(itemstack)){
					LiquidStack liquid = data.stillLiquid;
					int amount = ((ITankContainer)te).fill(ForgeDirection.getOrientation(facing.ordinal()).getOpposite(), liquid, false);
					if(liquid.amount == amount){
						((ITankContainer)te).fill(ForgeDirection.getOrientation(facing.ordinal()).getOpposite(), liquid, true);
						ItemStack result = itemstack.getItem().getContainerItemStack(itemstack);
						itemstack.stackSize--;
						if(result == null){
							result = data.container.copy();
							result.stackSize = 0;
						}
						return result;
					}else{
						return null;
					}
				}
			}
		}
		System.out.println("Not matched");
		return null;
	}

	
}