package lcdispenser;

import net.minecraft.src.BehaviorDefaultDispenseItem;
import net.minecraft.src.EnumFacing;
import net.minecraft.src.IBehaviorDispenseItem;
import net.minecraft.src.IBlockSource;
import net.minecraft.src.ItemStack;
import net.minecraft.src.TileEntityDispenser;

public abstract class BehaviorContainerDispense implements IBehaviorDispenseItem
{
	protected final IBehaviorDispenseItem chain;
	protected final static IBehaviorDispenseItem defaultDispenseBehavior = new BehaviorDefaultDispenseItem();
	
	public BehaviorContainerDispense(IBehaviorDispenseItem chain)
	{
		if(chain == null){
			this.chain = defaultDispenseBehavior;
		}else{
			this.chain = chain;
		}
	}
	
	@Override
	public ItemStack dispense(IBlockSource blockSource, ItemStack itemstack)
	{
		ItemStack result = dispenseStack(blockSource, itemstack);
		if(result == null){
			System.out.println("Not processed. Chain to Vanilla.");
			return chain.dispense(blockSource, itemstack);
		}
		if(result.stackSize <= 0){
			// The result was empty.
		}else if(addResultToInventory((TileEntityDispenser)blockSource.func_82619_j(), result)){
			// The result was placed in inventory.
		}else if(itemstack.stackSize <= 0){
			itemstack.itemID = result.itemID;
			itemstack.setItemDamage(result.getItemDamage());
			itemstack.stackSize = result.stackSize;
		}else{
			defaultDispenseBehavior.dispense(blockSource, result);
		}
		playDispenseSound(blockSource);
		spawnDispenseParticles(blockSource);
		return itemstack;
	}

	public abstract ItemStack dispenseStack(IBlockSource blockSource, ItemStack itemstack);
	
	private boolean addResultToInventory(TileEntityDispenser te, ItemStack result)
	{
		int size = te.getSizeInventory();
		int i;
		ItemStack itemstack;
		for(i=0; i<size; ++i){
			itemstack = te.getStackInSlot(i);
			if(itemstack != null && itemstack.isItemEqual(result) && itemstack.stackSize + result.stackSize <= itemstack.getMaxStackSize()){
				itemstack.stackSize += result.stackSize;
				return true;
			}
		}
		for(i=0; i<size; ++i){
			itemstack = te.getStackInSlot(i);
			if(itemstack == null || itemstack.itemID == 0){
				te.setInventorySlotContents(i, result);
				return true;
			}
		}
		return false;
	}
	
	protected void playDispenseSound(IBlockSource blockSource)
	{
		blockSource.getWorld().playAuxSFX(1000, blockSource.getXInt(), blockSource.getYInt(), blockSource.getZInt(), 0);
	}
	
	protected void spawnDispenseParticles(IBlockSource blockSource)
	{
		EnumFacing facing = EnumFacing.func_82600_a(blockSource.func_82620_h());
		blockSource.getWorld().playAuxSFX(2000, blockSource.getXInt(), blockSource.getYInt(), blockSource.getZInt(), facing.func_82601_c() + 1 + (facing.func_82599_e() + 1) * 3);
	}
	
}
