package xy177.inspectanimations;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.network.NetworkCheckHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.Logger;
import xy177.inspectanimations.common.CommonEventHandler;
import xy177.inspectanimations.network.NetworkHandler;
import xy177.inspectanimations.proxy.CommonProxy;

import java.util.Map;

@Mod(
        modid = InspectAnimations.MOD_ID,
        name = InspectAnimations.NAME,
        version = InspectAnimations.VERSION,
        acceptedMinecraftVersions = "[1.12.2]",
        dependencies = "required-after:mixinbooter@[11.5,)"
)
public class InspectAnimations {
    public static final String MOD_ID = "inspectanimations";
    public static final String NAME = "Inspect Animations";
    public static final String VERSION = "0.2.1";

    @SidedProxy(
            clientSide = "xy177.inspectanimations.proxy.ClientProxy",
            serverSide = "xy177.inspectanimations.proxy.CommonProxy"
    )
    public static CommonProxy PROXY;

    public static Logger LOGGER;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LOGGER = event.getModLog();
        NetworkHandler.init();
        MinecraftForge.EVENT_BUS.register(new CommonEventHandler());
        PROXY.preInit();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        PROXY.init();
    }

    @NetworkCheckHandler
    public boolean checkRemoteVersions(Map<String, String> remoteVersions, Side remoteSide) {
        String remoteVersion = remoteVersions.get(MOD_ID);
        return remoteVersion == null || VERSION.equals(remoteVersion);
    }
}
