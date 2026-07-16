package xy177.inspectanimationsantique;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.network.NetworkCheckHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.Logger;
import xy177.inspectanimationsantique.common.CommonEventHandler;
import xy177.inspectanimationsantique.network.NetworkHandler;
import xy177.inspectanimationsantique.proxy.CommonProxy;

import java.util.Map;

@Mod(
        modid = InspectAnimationsAntique.MOD_ID,
        name = InspectAnimationsAntique.NAME,
        version = InspectAnimationsAntique.VERSION,
        acceptedMinecraftVersions = "[1.12.2]",
        dependencies = "required-after:mixinbooter@[10.7,)"
)
public class InspectAnimationsAntique {
    public static final String MOD_ID = "inspect_animations_antique";
    public static final String NAME = "Inspect Animations Antique";
    public static final String VERSION = "0.2.1a";

    @SidedProxy(
            clientSide = "xy177.inspectanimationsantique.proxy.ClientProxy",
            serverSide = "xy177.inspectanimationsantique.proxy.CommonProxy"
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
