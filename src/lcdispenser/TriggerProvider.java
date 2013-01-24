package lcdispenser;

import java.util.LinkedList;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.liquids.ITankContainer;
import buildcraft.api.gates.ActionManager;
import buildcraft.api.gates.ITrigger;
import buildcraft.api.gates.ITriggerProvider;
import buildcraft.api.transport.IPipe;

public class TriggerProvider implements ITriggerProvider {

	private TriggerCanEmptyContainer triggerCanEmptyContiner = null;
	private TriggerCanFillContainer triggerCanFillContiner = null;
	
	public TriggerProvider() {
		if(ModConfiguration.emptyContainer > 64
				&& ModConfiguration.emptyContainer < ActionManager.triggers.length)
			triggerCanEmptyContiner = new TriggerCanEmptyContainer(ModConfiguration.emptyContainer);
		if(ModConfiguration.fillContainer > 64
				&& ModConfiguration.fillContainer < ActionManager.triggers.length)
			triggerCanFillContiner = new TriggerCanFillContainer(ModConfiguration.fillContainer);
	}

	@Override
	public LinkedList<ITrigger> getPipeTriggers(IPipe pipe) {
		return null;
	}

	@Override
	public LinkedList<ITrigger> getNeighborTriggers(Block block, TileEntity tile) {
		if(tile != null && tile instanceof ITankContainer)
		{
			LinkedList<ITrigger> result = new LinkedList<ITrigger>();
			if(triggerCanEmptyContiner != null) result.add(triggerCanEmptyContiner);
			if(triggerCanFillContiner != null) result.add(triggerCanFillContiner);
			return result;
		}
		return null;
	}

}
