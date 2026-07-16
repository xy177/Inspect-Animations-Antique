package xy177.inspectanimationsantique.core;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import zone.rong.mixinbooter.IEarlyMixinLoader;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@IFMLLoadingPlugin.Name("InspectAnimationsAntiqueMixinLoader")
@IFMLLoadingPlugin.MCVersion("1.12.2")
@IFMLLoadingPlugin.TransformerExclusions("xy177.inspectanimationsantique.core")
public final class InspectAnimationsMixinLoader implements IFMLLoadingPlugin, IEarlyMixinLoader {
    private static final String MIXIN_CONFIG = "mixins.inspect_animations_antique.json";

    @Override
    public List<String> getMixinConfigs() {
        return Collections.singletonList(MIXIN_CONFIG);
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[0];
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
