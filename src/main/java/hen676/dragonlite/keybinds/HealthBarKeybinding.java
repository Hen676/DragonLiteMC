package hen676.dragonlite.keybinds;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class HealthBarKeybinding extends DragonLiteKeybinding {
    private static KeyBinding keyBinding;
    private static boolean toggle = false;

    public static void init() {
        keyBinding = register("key.dragonlite.health_bar", GLFW.GLFW_KEY_KP_2);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keyBinding.wasPressed()) {
                if (client.world == null)
                    return;
                toggle = !toggle;
                if (client.player != null) {
                    MutableText text = Text.translatable("message.dragonlite.health_bar")
                            .styled(style -> style.withColor(TEXT_COLOR))
                            .append(" ");
                    if (toggle)
                        text.append(ON);
                    else
                        text.append(OFF);
                    client.player.sendMessage(text,true);
                }
            }
        });
    }

    public static boolean isHealthBar() {
        return toggle;
    }
}
