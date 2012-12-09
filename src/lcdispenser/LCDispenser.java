package lcdispenser;

import net.minecraft.src.BlockDispenser;
import net.minecraft.src.IBehaviorDispenseItem;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.Mod.ServerStarted;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.network.NetworkMod;
import net.minecraftforge.liquids.LiquidContainerData;
import net.minecraftforge.liquids.LiquidContainerRegistry;

@Mod(name="LCDispenser", version="0.0.0", modid = "LCDispenser")
@NetworkMod(clientSideRequired=true, serverSideRequired=false)
public class LCDispenser {
	
	@Instance("LCDispenser")
	public static LCDispenser instance;
	
	@PreInit
	public void preInit(FMLPreInitializationEvent event) {
	}
	
	@Init
	public void load(FMLInitializationEvent event) {
	}
	
	@PostInit
	public void postInit(FMLPostInitializationEvent event) {
	}
	
	@ServerStarted
	public void serverStarted(FMLServerStartedEvent event)
	{
		int i = 0;
		for(LiquidContainerData data: LiquidContainerRegistry.getRegisteredLiquidContainerData()){
			IBehaviorDispenseItem behavior = (IBehaviorDispenseItem) BlockDispenser.dispenseBehaviorRegistry.func_82594_a(data.container.getItem());
			if(!(behavior instanceof BehaviorContainerDispense)){
				BlockDispenser.dispenseBehaviorRegistry.putObject(data.container.getItem(), new BehaviorContainerEmptyDispense(behavior));
				++i;
			}
			behavior = (IBehaviorDispenseItem) BlockDispenser.dispenseBehaviorRegistry.func_82594_a(data.filled.getItem());
			if(!(behavior instanceof BehaviorContainerDispense)){
				BlockDispenser.dispenseBehaviorRegistry.putObject(data.filled.getItem(), new BehaviorContainerFullDispense(behavior));
				++i;
			}
		}
		System.out.println(i + " behaviors registered.");
	}
}
