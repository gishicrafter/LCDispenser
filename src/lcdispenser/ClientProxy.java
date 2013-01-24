package lcdispenser;

import net.minecraftforge.client.MinecraftForgeClient;

public class ClientProxy extends CommonProxy {

	@Override
	public void registerTextures() {
		MinecraftForgeClient.preloadTexture(CommonProxy.GATES_PNG);
	}

}
