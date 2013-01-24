package lcdispenser;

import java.io.File;
import lcdispenser.util.ConfigHelper;

public class ModConfiguration {
	
	private static final String CATEGORY_TRIGGER = "gate.trigger";

	@ConfigHelper.Value(category=CATEGORY_TRIGGER)
	public static int fillContainer = 350;

	@ConfigHelper.Value(category=CATEGORY_TRIGGER)
	public static int emptyContainer = 351;
	
	public static void loadConfiguration(File file)
	{
		ConfigHelper helper = new ConfigHelper(file);
		helper.loadTo(ModConfiguration.class);
	}
}
